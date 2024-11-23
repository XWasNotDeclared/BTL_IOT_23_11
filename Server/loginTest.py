from flask import Flask, request, jsonify
import mysql.connector
import bcrypt
import jwt
import datetime
from functools import wraps

app = Flask(__name__)

# Khóa bí mật cho JWT
SECRET_KEY = "your_secret_key_here"

# Kết nối đến MySQL
db = mysql.connector.connect(
    host="localhost",        # Thay bằng địa chỉ máy chủ của bạn nếu không phải localhost
    user="root",             # Tên đăng nhập MySQL
    password="????????????",     # Mật khẩu MySQL
    database="iotv01"        # Tên cơ sở dữ liệu
)

# Hàm để kiểm tra token
def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')
        
        if not token:
            return jsonify({"error": "Token is missing!"}), 401

        try:
            # Loại bỏ "Bearer " nếu token ở định dạng "Bearer <token>"
            if token.startswith("Bearer "):
                token = token.split(" ")[1]
            
            # Giải mã token
            data = jwt.decode(token, SECRET_KEY, algorithms=["HS256"])
            request.user = data['username']  # Lưu thông tin người dùng từ token
        except jwt.ExpiredSignatureError:
            return jsonify({"error": "Token has expired!"}), 401
        except jwt.InvalidTokenError:
            return jsonify({"error": "Invalid token!"}), 401

        return f(*args, **kwargs)
    return decorated

# Route đăng ký người dùng
@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    username = data['username']
    password = data['password']
    phone_number = data.get('phoneNumber', None)
    name = data.get('name', None)
    age = data.get('age', None)
    fcm = data.get('FCM', None)

    # Kiểm tra xem người dùng đã tồn tại chưa
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT * FROM user WHERE username = %s", (username,))
    existing_user = cursor.fetchone()

    if existing_user:
        return jsonify({"error": "Username already exists!"}), 400

    # Mã hóa mật khẩu
    hashed_password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())

    # Thêm người dùng vào cơ sở dữ liệu
    cursor.execute("""
        INSERT INTO user (username, password, phoneNumber, FCM, name, age)
        VALUES (%s, %s, %s, %s, %s, %s)
    """, (username, hashed_password, phone_number, fcm, name, age))
    db.commit()

    return jsonify({"message": "User registered successfully!"}), 201


# Route đăng nhập người dùng
@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data['username']
    password = data['password']
    fcm = data.get('FCM', None)

    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT * FROM user WHERE username = %s", (username,))
    user = cursor.fetchone()

    if user and bcrypt.checkpw(password.encode('utf-8'), user['password'].encode('utf-8')):
        # Cập nhật FCM nếu cần
        if fcm and user['FCM'] != fcm:
            cursor.execute("UPDATE user SET FCM = %s WHERE uid = %s", (fcm, user['uid']))
            db.commit()

        # Tạo token
        token = jwt.encode(
            {
                "username": username,
                "exp": datetime.datetime.utcnow() + datetime.timedelta(hours=1)
            },
            SECRET_KEY,
            algorithm="HS256"
        )
        return jsonify({"message": "Login successful!", "token": token}), 200

    return jsonify({"error": "Invalid credentials"}), 401


# Route để lấy danh sách tất cả thiết bị
@app.route('/allDevices', methods=['GET'])
@token_required
def get_devices():
    # Kết nối cơ sở dữ liệu và truy vấn tất cả thiết bị
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT id, deviceName FROM device")

    devices = cursor.fetchall()

    if not devices:
        return jsonify({"message": "No devices found."}), 404

    # Trả về danh sách thiết bị dưới dạng JSON
    return jsonify({"devices": devices}), 200
@app.route('/allDevices', methods=['GET'])
@token_required
def get_devices():
    cursor = db.cursor(dictionary=True)
    cursor.execute("""
        SELECT 
            device.id AS deviceId,
            device.deviceName,
            user.uid AS userId,
            user.username AS userName
        FROM 
            device
        LEFT JOIN 
            user
        ON 
            device.userUid = user.uid;
        """)
    devices = cursor.fetchall()

    if not devices:
        return jsonify({"message": "No devices found."}), 404

    return jsonify({"devices": devices}), 200
#Lấy thiết bị đang theo dõi
@app.route('/followingDevices', methods=['GET'])
@token_required
def following_devices():
    # Lấy thông tin user từ token
    username = request.user  # Đảm bảo `request.user` chứa username từ token

    # Kết nối cơ sở dữ liệu và truy vấn tất cả thiết bị mà user đang follow
    cursor = db.cursor(dictionary=True)
    cursor.execute("""
        SELECT 
            device.id AS deviceId,
            device.deviceName AS deviceName,
            device.userUid AS userId,
            owner.username AS userName
        FROM 
            follow
        JOIN 
            device
        ON 
            follow.deviceId = device.id
        JOIN 
            user AS owner
        ON 
            device.userUid = owner.uid
        WHERE 
            follow.userUid = (SELECT uid FROM user WHERE username = %s)
            AND follow.status = %s
    """, (username, 'active'))

    devices = cursor.fetchall()

    if not devices:
        return jsonify({"message": "You are not following any devices."}), 404

    return jsonify({"devices": devices}), 200

#Lấy người dùng gửi follw nhưng người chủ thiết bị không đồng ý
@app.route('/pendingDevices', methods=['GET'])
@token_required
def pending_devices():
    # Lấy thông tin user từ token
    username = request.user  # Đảm bảo `request.user` chứa username từ token

    # Kết nối cơ sở dữ liệu và truy vấn tất cả thiết bị mà user đang follow
    cursor = db.cursor(dictionary=True)
    cursor.execute("""
        SELECT 
            device.id AS deviceId,
            device.deviceName AS deviceName,
            device.userUid AS userId,
            owner.username AS userName
        FROM 
            follow
        JOIN 
            device
        ON 
            follow.deviceId = device.id
        JOIN 
            user AS owner
        ON 
            device.userUid = owner.uid
        WHERE 
            follow.userUid = (SELECT uid FROM user WHERE username = %s)
            AND follow.status = %s
    """, (username, 'inactive'))

    devices = cursor.fetchall()

    if not devices:
        return jsonify({"message": "You are not following any devices."}), 404

    return jsonify({"devices": devices}), 200

# Lấy lịch sử thiết bị
@app.route('/deviceFollowingHistory', methods=['GET'])
@token_required
def device_history():
    # Lấy thông tin user từ token
    username = request.user  # Đảm bảo `request.user` chứa username từ token

    # Kết nối cơ sở dữ liệu và truy vấn tất cả lịch sử thiết bị mà user đang follow
    cursor = db.cursor(dictionary=True)
    cursor.execute("""
        SELECT 
            dh.id AS id,
            dh.date AS date,
            dh.longitude AS longitude,
            dh.latitude AS latitude,
            dh.moreInfor AS moreInfo,
            d.id AS deviceId,
            d.deviceName,
            u.uid AS userId,
            u.username AS deviceOwnerUsername
        FROM 
            follow f
        JOIN 
            device d ON f.deviceId = d.id
        JOIN 
            device_history dh ON d.id = dh.deviceId
        JOIN 
            user u ON d.userUid = u.uid
        WHERE 
            f.userUid = (SELECT uid FROM user WHERE username = %s) 
            AND f.status = 'active'
    """, (username,))

    device_histories = cursor.fetchall()

    if not device_histories:
        return jsonify({"message": "You are not following any devices or no history found."}), 404

    return jsonify({"deviceHistories": device_histories}), 200



#Lấy dang sách thiết bị CHƯA THEO DÕI
@app.route('/unfollowingDevices', methods=['GET'])
@token_required
def unfollowing_devices():
    # Lấy thông tin username từ token
    username = request.user

    # Kết nối cơ sở dữ liệu để lấy user ID từ username
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT uid FROM user WHERE username = %s", (username,))
    user = cursor.fetchone()

    if not user:
        return jsonify({"error": "User not found"}), 404

    user_id = user['uid']

    # Truy vấn các thiết bị mà người dùng chưa follow, cùng với userId và username của người dùng liên kết với thiết bị
    cursor.execute("""
        SELECT device.id AS deviceId, device.deviceName, user.uid AS userId, user.userName
        FROM device
        LEFT JOIN follow ON device.id = follow.deviceId AND follow.userUid = %s
        LEFT JOIN user ON device.userUid = user.uid
        WHERE follow.id IS NULL
    """, (user_id,))

    devices = cursor.fetchall()

    if not devices:
        return jsonify({"message": "All devices are already followed."}), 200

    return jsonify({"devices": devices}), 200

#Lấy danh sách thiết bị mà bản thân làm chủ
@app.route('/myDevices', methods=['GET'])
@token_required
def my_devices():
    # Lấy thông tin username từ token
    username = request.user

    # Kết nối cơ sở dữ liệu để lấy user ID từ username
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT uid FROM user WHERE username = %s", (username,))
    user = cursor.fetchone()

    if not user:
        return jsonify({"error": "User not found"}), 404

    user_id = user['uid']

    # Truy vấn các thiết bị mà người dùng chưa follow, cùng với userId và username của người dùng liên kết với thiết bị
    cursor.execute("""
        SELECT device.id AS deviceId, device.deviceName, user.uid AS userId, user.userName
        FROM device
        LEFT JOIN user ON device.userUid = user.uid
        WHERE device.userUid = %s
    """, (user_id,))

    devices = cursor.fetchall()

    if not devices:
        return jsonify({"message": "All devices are already followed."}), 200

    return jsonify({"devices": devices}), 200

#Theo dõi thiết bị
@app.route('/followDevice', methods=['POST'])
@token_required
def follow_device():
    # Lấy thông tin username từ token
    username = request.user

    # Lấy deviceId từ dữ liệu POST request
    device_id = request.json.get('deviceId')

    if not device_id:
        return jsonify({"error": "Device ID is required"}), 400

    # Kết nối cơ sở dữ liệu để lấy user ID từ username
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT uid FROM user WHERE username = %s", (username,))
    user = cursor.fetchone()

    if not user:
        return jsonify({"error": "User not found"}), 404

    user_id = user['uid']

    # Kiểm tra nếu user đã follow thiết bị này rồi
    cursor.execute("SELECT * FROM follow WHERE userUid = %s AND deviceId = %s", (user_id, device_id))
    existing_follow = cursor.fetchone()

    if existing_follow:
        return jsonify({"error": "You are already following this device."}), 400

    # Thêm bản ghi follow mới vào bảng follow với trạng thái 'active'
    cursor.execute("""
        INSERT INTO follow (status, userUid, deviceId)
        VALUES (%s, %s, %s)
    """, ('active', user_id, device_id))

    # Commit thay đổi vào cơ sở dữ liệu
    db.commit()

    return jsonify({"message": "Device followed successfully with status 'active'."}), 201
#Thêm thiết bị mới
@app.route('/addDevice', methods=['POST'])
@token_required
def add_device():
    # Lấy thông tin username từ token
    username = request.user

    # Lấy dữ liệu từ request body
    data = request.get_json()
    device_name = data.get('deviceName')

    if not device_name:
        return jsonify({"error": "Device name is required"}), 400

    # Kết nối cơ sở dữ liệu để lấy user ID từ username
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT uid FROM user WHERE username = %s", (username,))
    user = cursor.fetchone()

    if not user:
        return jsonify({"error": "User not found"}), 404

    user_id = user['uid']

    # Kiểm tra xem tên thiết bị đã tồn tại hay chưa
    cursor.execute("SELECT * FROM device WHERE deviceName = %s AND userUid = %s", (device_name, user_id))
    existing_device = cursor.fetchone()

    if existing_device:
        return jsonify({"error": "You already have a device with this name."}), 400

    # Thêm thiết bị mới vào bảng `device`
    cursor.execute("""
        INSERT INTO device (deviceName, userUid)
        VALUES (%s, %s)
    """, (device_name, user_id))
    db.commit()

    return jsonify({"message": "Device added successfully!"}), 201



# Route kiểm tra server
@app.route('/ping', methods=['GET'])
def ping():
    return jsonify({"message": "Server is up and running!"}), 200
##############################ESP############################
import datetime
from adminFB import send_fcm_message



# Thêm lịch sử thiết bị (khi bị ngã xe, route này của esp32)
@app.route('/addDeviceHistory', methods=['POST'])
def add_device_history():
    # Lấy dữ liệu từ request body
    data = request.get_json()
    device_name = data.get('device')
    longitude = data.get('longitude')
    latitude = data.get('latitude')
    more_info = data.get('moreInfor')


    # Kết nối cơ sở dữ liệu và lấy ID thiết bị từ tên thiết bị
    cursor = db.cursor(dictionary=True)
    cursor.execute("""
        SELECT id, userUid FROM device WHERE deviceName = %s
    """, (device_name,))
    device = cursor.fetchone()

    if not device:
        return jsonify({"error": "Device not found."}), 404

    device_id = device['id']

    # Lấy ngày giờ hiện tại
    current_date = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

    # Thêm bản ghi vào bảng device_history
    cursor.execute("""
        INSERT INTO device_history (date, longitude, latitude, moreInfor, deviceId)
        VALUES (%s, %s, %s, %s, %s)
    """, (current_date, longitude, latitude, more_info, device_id))

    # Commit thay đổi vào cơ sở dữ liệu
    db.commit()

    # Lấy danh sách FCM, deviceName và username của tất cả user đang follow thiết bị
    cursor.execute("""
        SELECT 
            u.FCM, 
            d.deviceName, 
            u.username AS owner_username
        FROM 
            follow f
        JOIN 
            device d ON f.deviceId = d.id
        JOIN 
            user u ON d.userUid = u.uid
        WHERE 
            f.deviceId = %s AND f.status = 'active'
    """, (device_id,))

    temp = cursor.fetchall()

    # Nếu không có người dùng nào theo dõi thiết bị, không gửi thông báo
    if not temp:
        print("khong co ai follow !!!")
        return jsonify({"message": "Device history added, but no followers found to notify."}), 201

    # Gửi thông báo đến tất cả các FCM token
    for t in temp:
        print("\ntokenFCM " + t['FCM'])
        dataInfor = {
            "longitude": str(longitude),
            "latitude": str(latitude)
        }
        
        send_fcm_message(t['FCM'], f"DETECTION DEVICE '{t['deviceName']}' FALL AT!!!", f"Owner: {t['owner_username']}\nlongitude: {longitude}\nlatitude: {latitude}\nClick to open in Googlemap", data=dataInfor)

    return jsonify({"message": "Device history added successfully."}), 201



if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)

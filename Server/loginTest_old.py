from flask import Flask, request, jsonify
import mysql.connector
import bcrypt

app = Flask(__name__)

# Kết nối đến MySQL
db = mysql.connector.connect(
    host="localhost",        # Thay bằng địa chỉ máy chủ của bạn nếu không phải localhost
    user="root",             # Tên đăng nhập MySQL
    password="06032003",     # Mật khẩu MySQL
    database="iotv01"  # Tên cơ sở dữ liệu
)

@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    username = data['username']
    password = data['password']
    
    # Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
    hashed_password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())
    
    cursor = db.cursor()
    cursor.execute("INSERT INTO users (username, password) VALUES (%s, %s)", (username, hashed_password))
    db.commit()
    
    return jsonify({"message": "User registered successfully!"}), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data['username']
    password = data['password']
    
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT * FROM users WHERE username = %s", (username,))
    user = cursor.fetchone()
    
    if user and bcrypt.checkpw(password.encode('utf-8'), user['password'].encode('utf-8')):
        return jsonify({"message": "Login successful!"}), 200
    else:
        return jsonify({"error": "Invalid credentials"}), 401

@app.route('/ping', methods=['GET'])
def ping():
    return jsonify({"message": "Server is up and running!"}), 200



if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)

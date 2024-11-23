import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging

cred = credentials.Certificate("iottest2-a8f3a-firebase-adminsdk-iq47z-2d97d08ed5.json")
firebase_admin.initialize_app(cred)

# Hàm gửi thông báo
def send_fcm_message(FCMtoken, title, body, data):
    # Tạo thông báo
    message = messaging.Message(
        notification=messaging.Notification(
            title=title,
            body=body,
        ),
        token=FCMtoken,  # Token của thiết bị nhận thông báo
        data=data  # Dữ liệu kèm theo thông báo
    )
    try:
        # Gửi thông báo
        response = messaging.send(message)
        print(f"Thông báo đã được gửi thành công! ID phản hồi: {response}")
    except Exception as e:
        print(f"Lỗi khi gửi thông báo: {e}")

# Token của thiết bị nhận thông báo (thay bằng token thực)
device_token = "fgdvktj1QLOuyp-t0X5Jyg:APA91bGZGjiXalvC0J16bC0GpeL8Qn8_c126z-ySTVID4F1goU6c8vzuRsoLDlQlPnYcpbc7Mc7O79K1ngV7aORMjyxxqPrE1OZtH-O5qLq-MnXHZOVLWRE"
data_payload = {
    "key1": "value1",
    "key2": "value2",
    "info": "Some additional information"
}
# Gửi thông báo
send_fcm_message(
    FCMtoken=device_token,
    title="Chào từ Firebase",
    body="Crash detection ",
    data=data_payload
)
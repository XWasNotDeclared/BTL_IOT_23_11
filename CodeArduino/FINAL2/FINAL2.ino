#include <Wire.h>
#include <MPU6050.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <TinyGPS++.h>


// Khởi tạo đối tượng GPS và sử dụng HardwareSerial cho giao tiếp với GPS
TinyGPSPlus gps;
HardwareSerial mySerial(1);  // Khởi tạo đối tượng HardwareSerial với UART1



MPU6050 mpu;

const int ledPin = 12;     // Chân xuất tín hiệu (thay đổi nếu cần)
const int buttonPin = 14;  // Chân nút nhấn (thay đổi nếu cần)

bool ledState = LOW;                    // Trạng thái LED
unsigned long stationaryStartTime = 0;  // Thời gian bắt đầu vật đứng yên
bool isStationary = false;              // Trạng thái vật đứng yên

// Đặt thông tin WiFi
const char* WIFI_SSID = "Mai Mai T3";        // Thay "your_SSID" bằng tên WiFi của bạn
const char* WIFI_PASSWORD = ""; // Thay "your_PASSWORD" bằng mật khẩu WiFi của bạn


// URL của server Flask
const String url = "http://192.168.0.128:5000/addDeviceHistory";

// Thêm hàm để gửi yêu cầu POST khi ledState == HIGH
void sendDeviceHistory(String device_name, float longitude, float latitude, String more_info) {
  HTTPClient http;

  String payload = "{\"device\":\"" + device_name + "\",\"longitude\":" + String(longitude, 6) + ",\"latitude\":" + String(latitude, 6) + ",\"moreInfor\":\"" + more_info + "\"}";

  http.begin(url); // Bắt đầu kết nối đến server Flask
  http.addHeader("Content-Type", "application/json"); // Định dạng dữ liệu là JSON

  int httpResponseCode = http.POST(payload); // Gửi yêu cầu POST

  if (httpResponseCode > 0) {
    Serial.println("POST request sent successfully");
    String response = http.getString();
    Serial.println(response);  // In ra phản hồi từ server
  } else {
    Serial.println("Error in sending POST request");
  }

  http.end(); // Đóng kết nối
}
// Mảng lưu 5 giá trị gần nhất của gia tốc và con quay hồi chuyển
float axValues[5] = {0}, ayValues[5] = {0}, azValues[5] = {0};
float gxValues[5] = {0}, gyValues[5] = {0}, gzValues[5] = {0};

// Hàm để cập nhật giá trị vào mảng (FIFO - giữ 5 giá trị gần nhất)
void updateValues(float* array, float newValue) {
  for (int i = 4; i > 0; i--) {
    array[i] = array[i - 1];
  }
  array[0] = newValue;  // Cập nhật giá trị mới nhất vào đầu mảng
}

// Hàm tạo chuỗi dữ liệu cảm biến dạng "ax01: 1.0, ay01: 2.0,..., gx01: 0.1, gy01: 0.2,..."
String createSensorDataString() {
  String sensorData = "";
  for (int i = 0; i < 5; i++) {
    sensorData += "ax" + String(i + 1) + ": " + String(axValues[i], 2) + ", ";
    sensorData += "ay" + String(i + 1) + ": " + String(ayValues[i], 2) + ", ";
    sensorData += "az" + String(i + 1) + ": " + String(azValues[i], 2) + ", ";
    // sensorData += "gx" + String(i + 1) + ": " + String(gxValues[i], 2) + ", ";
    // sensorData += "gy" + String(i + 1) + ": " + String(gyValues[i], 2) + ", ";
    // sensorData += "gz" + String(i + 1) + ": " + String(gzValues[i], 2);
    if (i < 4) sensorData += ", ";  // Thêm dấu phẩy trừ giá trị cuối
  }
  return sensorData;
}
void setup() {
  // Khởi tạo kết nối Serial
  Serial.begin(115200);
  
  // Kết nối WiFi
////firebase setup//
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  //////////////////////
  Wire.begin();

  mpu.initialize();
  if (!mpu.testConnection()) {
    Serial.println("MPU6050 connection failed");
    while (1)
      ;
  }
  Serial.println("MPU6050 connection successful");

  pinMode(ledPin, OUTPUT);           // Đặt chân xuất tín hiệu làm đầu ra
  pinMode(buttonPin, INPUT_PULLUP);  // Đặt chân nút nhấn làm đầu vào với pull-up

  // Khởi tạo giao tiếp UART1 với GPS (chọn TX = 17, RX = 16)
  mySerial.begin(9600, SERIAL_8N1, 16, 17);  // Khởi tạo HardwareSerial1, RX = 16, TX = 17
}

void loop() {
  // Đọc giá trị gia tốc
  int16_t ax, ay, az, gx, gy, gz;
  mpu.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);

  // Chuyển đổi giá trị raw thành giá trị thực tế
  float accelX = ax / 16384.0;  // Giá trị gia tốc (g)
  float accelY = ay / 16384.0;
  float accelZ = az / 16384.0;
  float gyroX = gx / 131.0;     // Giá trị con quay hồi chuyển (°/s)
  float gyroY = gy / 131.0;
  float gyroZ = gz / 131.0;

  // Cập nhật giá trị vào mảng
  updateValues(axValues, accelX);
  updateValues(ayValues, accelY);
  updateValues(azValues, accelZ);
  updateValues(gxValues, gyroX);
  updateValues(gyValues, gyroY);
  updateValues(gzValues, gyroZ);

  // Tạo chuỗi dữ liệu
  String sensorData = createSensorDataString();

  // Tính tổng gia tốc
  float aTotal = sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

  // Kiểm tra xem vật có đang đứng yên không
  float threshold = 0.05;  // cận dưới (0.1 g)

  if (abs(aTotal - 1.0) < threshold) {
    // Vật có thể đang đứng yên
    Serial.println("Vật đang đứng yên");
    if (isStationary == false) {
      stationaryStartTime = millis();
      isStationary = true;
    }
    if (isStationary && (millis() - stationaryStartTime >= 2000)) {  // 2 giây

                                                                     // Kiểm tra xem vật có bị ngã không (gia tốc X hoặc Y lớn hơn 0.5 g)
      if (abs(accelX) > 0.5 || abs(accelY) > 0.5) {
        ledState = HIGH;  // Sáng đèn nếu ngã
        // Đọc dữ liệu GPS từ module Neo-6M
        while (mySerial.available() > 0) {
          gps.encode(mySerial.read());
        }

        // Nếu không có tín hiệu GPS, dùng tọa độ giả
        float latitude = 20.980934;
        float longitude = 105.786543;
        String locationMessage = "Fake Location";  // Dữ liệu giả

        if (gps.location.isUpdated()) {
          // Nếu có tín hiệu GPS, lấy tọa độ thực
          latitude = gps.location.lat();
          longitude = gps.location.lng();
          locationMessage = "Real Location";  // Cập nhật với thông tin thực
          Serial.print("Latitude= "); 
          Serial.print(latitude, 6);
          Serial.print(" Longitude= "); 
          Serial.println(longitude, 6);
        }



        // Gọi API khi ledState == HIGH
        sendDeviceHistory("Device 5", longitude, latitude, sensorData);
        //isStationary = false; // Vật không đứng yên
      }
    }
  } else {
    // Vật đang di chuyển
    Serial.println("Vật đang di chuyển");
    //ledState = LOW; // Không sáng đèn khi di chuyển
    isStationary = false;  // Đánh dấu là không đứng yên
  }

  // Nếu nút nhấn được nhấn, tắt đèn
  if (digitalRead(buttonPin) == LOW) {
    ledState = LOW;  // Tắt đèn khi nhấn nút
    isStationary=false;
    while (digitalRead(buttonPin) == LOW)
      ;  // Đợi thả nút
  }

  digitalWrite(ledPin, ledState);  // Cập nhật trạng thái LED
  Serial.println(accelX);
  Serial.println(accelY);
  Serial.println(accelZ);
  delay(500);  // Đợi một chút trước khi đọc lại
}

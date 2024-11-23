import mysql.connector
import csv

# Kết nối tới MySQL Database
def connect_to_db():
    try:
        # Thay đổi thông tin kết nối cho phù hợp với cơ sở dữ liệu của bạn
        conn = mysql.connector.connect(
            host="localhost",          # Địa chỉ máy chủ MySQL
            user="root",               # Tên người dùng
            password="06032003",  # Mật khẩu
            database="iotv01"          # Tên cơ sở dữ liệu
        )
        return conn
    except mysql.connector.Error as err:
        print(f"Lỗi kết nối tới MySQL: {err}")
        return None

# Truy vấn dữ liệu từ cơ sở dữ liệu
def get_more_infor_data(conn):
    try:
        cursor = conn.cursor()
        query = "SELECT moreInfor FROM device_history"
        cursor.execute(query)
        result = cursor.fetchall()
        return result
    except mysql.connector.Error as err:
        print(f"Lỗi truy vấn: {err}")
        return []

# Xử lý dữ liệu từ chuỗi moreInfor
def parse_more_infor(more_infor):
    # Xóa dấu cách và ngắt dòng thừa
    cleaned_data = more_infor.replace(", ,", ",").strip()
    # Tách thành các cặp khóa:giá trị
    pairs = cleaned_data.split(", ")
    records = {}
    
    # Xử lý từng cặp khóa:giá trị
    for pair in pairs:
        if pair:  # Bỏ qua các cặp rỗng
            key, value = pair.split(": ")
            # Loại bỏ dấu phẩy và khoảng trắng không cần thiết từ giá trị
            value = value.strip().replace(",", "")
            try:
                # Chuyển giá trị thành số thực và lưu vào từ điển
                records[key.strip()] = float(value)
            except ValueError:
                # Nếu có lỗi trong việc chuyển đổi, in ra cảnh báo và bỏ qua giá trị này
                print(f"Cảnh báo: Không thể chuyển đổi giá trị '{value}' thành float.")
    
    return records

# Xuất dữ liệu ra file CSV
def export_to_csv(data, filename):
    # Nếu không có dữ liệu, không làm gì cả
    if not data:
        print("Không có dữ liệu để xuất.")
        return
    
    # Lấy danh sách các tên cột (tất cả các khóa từ dữ liệu)
    headers = sorted(set(key for row in data for key in row.keys()))
    
    with open(filename, mode='w', newline='', encoding='utf-8') as file:
        writer = csv.DictWriter(file, fieldnames=headers)
        # Ghi tiêu đề cột
        writer.writeheader()
        # Ghi từng dòng dữ liệu
        writer.writerows(data)

# Chương trình chính
def main():
    # Kết nối tới cơ sở dữ liệu
    conn = connect_to_db()
    if conn is None:
        return
    
    # Lấy dữ liệu từ bảng device_history
    data_from_db = get_more_infor_data(conn)
    
    # Dữ liệu sẽ được xử lý và xuất thành CSV
    all_parsed_data = []
    for row in data_from_db:
        more_infor = row[0]
        parsed_data = parse_more_infor(more_infor)
        all_parsed_data.append(parsed_data)  # Thêm dữ liệu đã xử lý vào danh sách chung
    
    # Xuất dữ liệu ra file CSV
    export_to_csv(all_parsed_data, 'more_infor_output.csv')
    
    print("Dữ liệu đã được xuất ra file 'more_infor_output.csv'")
    
    # Đóng kết nối
    conn.close()

if __name__ == "__main__":
    main()

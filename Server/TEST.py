import pandas as pd
import random
import math

# Hàm sinh dữ liệu ngẫu nhiên và kiểm tra điều kiện
def generate_data_with_constraint(num_records):
    data = {
        'ax1': [],
        'ax2': [],
        'ax3': [],
        'ax4': [],
        'ax5': [],
        'ay1': [],
        'ay2': [],
        'ay3': [],
        'ay4': [],
        'ay5': [],
        'az1': [],
        'az2': [],
        'az3': [],
        'az4': [],
        'az5': [],
        'TAG': []
    }
    
    for _ in range(num_records):
        # Sinh giá trị ngẫu nhiên cho ax, ay, az sao cho căn của tổng bình phương không vượt quá 1
        while True:
            ax = round(random.uniform(0, 1), 2)  # Lấy 2 chữ số sau dấu phẩy
            ay = round(random.uniform(0, 1), 2)  # Lấy 2 chữ số sau dấu phẩy
            az = round(random.uniform(0, 1), 2)  # Lấy 2 chữ số sau dấu phẩy
            # Kiểm tra điều kiện căn của tổng bình phương không vượt quá 1
            if math.sqrt(ax**2 + ay**2 + az**2) <= 1:
                break
        
        # Gán giá trị vào các cột ax1, ay1, az1
        data['ax1'].append(ax)
        data['ay1'].append(ay)
        data['az1'].append(az)

        # Sinh các cột ax2, ax3, ... với giá trị ngẫu nhiên từ 0 đến 1 và lấy 2 chữ số sau dấu phẩy
        for i in range(2, 6):
            data[f'ax{i}'].append(round(random.uniform(0, 1), 2))
            data[f'ay{i}'].append(round(random.uniform(0, 1), 2))
            data[f'az{i}'].append(round(random.uniform(0, 1), 2))
        
        # TAG: Chọn ngẫu nhiên trong ['Nhe', 'Nang', 'Nghiem']
        data['TAG'].append(random.choice(['Nhe', 'Nang', 'NghiemTrong']))

    # Tạo DataFrame từ dữ liệu
    df = pd.DataFrame(data)
    return df

# Sinh 1000 bản ghi dữ liệu với điều kiện
num_records = 1000
df = generate_data_with_constraint(num_records)

# Xuất dữ liệu ra file CSV
df.to_csv('output_data_with_constraint_2_decimal.csv', index=False)

print(f"Đã sinh {num_records} bản ghi và lưu vào 'output_data_with_constraint_2_decimal.csv'.")

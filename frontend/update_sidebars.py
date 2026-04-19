import os
import re

target_dir = r'd:\garaoto\frontend\pages\admin'
files = [
    'index.html', 'duyet-lich.html', 'don-thue.html', 
    'phieu-sua-chua.html', 'phan-cong.html', 'dich-vu.html', 'khach-hang.html'
]

link_to_insert = '''
                <a href="/pages/admin/xe-cho-thue.html" class="admin-nav-item">
                    <i class="fa-solid fa-car-side"></i> Xe Cho Thuê
                </a>'''

for f in files:
    path = os.path.join(target_dir, f)
    if os.path.exists(path):
        with open(path, 'r', encoding='utf-8') as file:
            content = file.read()
        
        if 'xe-cho-thue.html' not in content:
            # We insert after Don Thue Xe
            pattern = re.compile(r'(<a href="/pages/admin/don-thue\.html" class="admin-nav-item[^"]*">\s*<i class="fa-solid fa-file-contract"></i> Đơn Thuê Xe\s*</a>)', re.DOTALL)
            match = pattern.search(content)
            if match:
                new_content = content[:match.end()] + link_to_insert + content[match.end():]
                with open(path, 'w', encoding='utf-8') as file:
                    file.write(new_content)
                print(f'Updated {f}')
            else:
                print(f'Could not find insertion point in {f}')
        else:
            print(f'Already has link in {f}')

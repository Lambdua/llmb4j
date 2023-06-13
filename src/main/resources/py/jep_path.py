import glob
import os
import platform
import site

# 获取操作系统类型：
os_type = platform.system()
str = "jep/libjep.*"
if os_type == 'Linux':
    # 处理Linux操作系统
    pass
elif os_type == 'Windows':
    # 如果操作系统是windows
    str = "jep\jep.dll"
    pass
for path in site.getsitepackages():
    for f in glob.glob(os.path.join(path, str)):
        print(f)

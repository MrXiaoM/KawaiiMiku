@echo off

set host=0.0.0.0
set port=8080
set cpu_count=2
set android_id=ABCDABCDABCDABCD

echo.KawaiiMiku
echo.API: http://%host%:%port%
echo.cpu count: %cpu_count%
echo.Android ID: %android_id%
echo.
set /p version="Please input txlib version: "
echo.
cmd /C bin\unidbg-fetch-qsign --host=%host% --port=%port% --count=%cpu_count% --library=txlib\%version% --android_id=%android_id%
pause
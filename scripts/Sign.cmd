@echo off
echo.KawaiiMiku
set /p version="Please input txlib version: "
echo.
bin\unidbg-fetch-qsign --basePath=txlib/%version%
pause
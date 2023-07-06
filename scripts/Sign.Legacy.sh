
host="0.0.0.0"
port="8080"
cpu_count="2"
android_id="ABCDABCDABCDABCD"

echo KawaiiMiku
echo "API: http://$host:$port"
echo "cpu count: $cpu_count"
echo "Android ID: $android_id"
echo ""
read -p "Please input txlib version: " version
echo
bin\unidbg-fetch-qsign --host=$host --port=$port --count=$cpu_count --library=txlib/$version --android_id=$android_id

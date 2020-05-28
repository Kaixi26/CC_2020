#/bin/sh
java --class-path out/production/gwanon/ Main --port=12345 --udp-port=6666 --server-port=22 --hops=2 --server-ip=10.4.4.1 '--gateways=10.3.3.2:6666;10.3.3.3:6666'





*** Starting nginx ***
In the command line, navigate to the nginx folder and run

```
docker run -it -p 666:666 -v $(pwd)/nginx.local:/etc/nginx/nginx.conf nginx
```
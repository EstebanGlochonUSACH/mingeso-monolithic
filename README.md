# DuckDNS url

URL: http://autofix-mingeso.duckdns.org/

Setup with Docker:

```sh
docker create \
  --name=duckdns \
  --restart=always \
  -e PUID=1000 \
  -e PGID=1000 \
  -e TZ=America/Santiago \
  -e SUBDOMAINS=autofix-mingeso \
  -e TOKEN=<token> \
  linuxserver/duckdns
```
```sh
docker start duckdns
```

References:

- Tutorial: https://www.jackofalladmins.com/admin%20adventures/home%20automation/duckdns-docker-setup/
- DuckDNS: https://www.duckdns.org/domains
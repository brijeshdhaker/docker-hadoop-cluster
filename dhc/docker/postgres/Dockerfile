#
# docker build -t dhc/postgres:latest -f Dockerfile .
#

FROM postgres:latest

# Specify the User that the actual main process will run as
USER root

RUN cd /

COPY docker/postgres/init-setup.sh /
RUN chmod -R 777 /init-setup.sh

# ENTRYPOINT ["/init-setup.sh"]

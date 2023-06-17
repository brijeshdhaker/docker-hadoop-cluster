#
# docker build -t brijeshdhaker/hbase-master:3.0.0 -f docker-hbase-3.0.0/hmaster/Dockerfile .
# docker push brijeshdhaker/hbase-master:3.0.0
#

FROM brijeshdhaker/hbase-base:3.0.0

COPY docker-hbase-3.0.0/hmaster/run.sh /run.sh
RUN chown hbase:hbase /run.sh && chmod 755 /run.sh

EXPOSE 16000 16010

# Specify the User that the actual main process will run as
USER hbase:hbase

CMD ["/run.sh"]
#
# docker build -t brijeshdhaker/hbase-master:2.4.9 -f docker-hbase/hmaster/Dockerfile .
# docker push brijeshdhaker/hbase-master:2.4.9
#

FROM brijeshdhaker/hbase-base:2.4.9

COPY docker-hbase/hmaster/run.sh /run.sh
RUN chown hbase:hbase /run.sh && chmod 755 /run.sh

EXPOSE 16000 16010

# Specify the User that the actual main process will run as
USER hbase:hbase

CMD ["/run.sh"]
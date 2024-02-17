#
# docker build -t brijeshdhaker/hbase-regionserver:2.4.9 -f docker-hbase/hregion/Dockerfile .
# docker push brijeshdhaker/hbase-regionserver:2.4.9
#
FROM brijeshdhaker/hbase-base:2.4.9

COPY docker-hbase/hregion/run.sh /run.sh
RUN chown hbase:hbase /run.sh && chmod 755 /run.sh

EXPOSE 16020 16030

# Specify the User that the actual main process will run as
USER hbase:hbase

CMD ["/run.sh"]
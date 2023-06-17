#
# docker build -t brijeshdhaker/hbase-regionserver:3.0.0 -f docker-hbase-3.0.0/hregion/Dockerfile .
# docker push brijeshdhaker/hbase-regionserver:3.0.0
#
FROM brijeshdhaker/hbase-base:3.0.0

COPY docker-hbase-3.0.0/hregion/run.sh /run.sh
RUN chown hbase:hbase /run.sh && chmod 755 /run.sh

EXPOSE 16020 16030

# Specify the User that the actual main process will run as
USER hbase:hbase

CMD ["/run.sh"]
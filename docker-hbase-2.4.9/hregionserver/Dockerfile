#
# docker build -t brijeshdhaker/hbase-regionserver:2.4.9 -f docker-hbase-2.4.9/hregionserver/Dockerfile .
# docker push brijeshdhaker/hbase-regionserver:2.4.9
#
FROM brijeshdhaker/hbase-base:2.4.9

COPY docker-hbase-2.4.9/hregionserver/run.sh /run.sh
RUN chmod +x /run.sh

EXPOSE 16020 16030

CMD ["/run.sh"]
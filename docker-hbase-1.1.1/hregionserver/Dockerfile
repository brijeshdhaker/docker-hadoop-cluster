FROM brijeshdhaker/hbase-base:1.1.1

COPY docker-hbase-1.1.1/hregionserver/run.sh /run.sh
RUN chmod +x /run.sh

EXPOSE 16020 16030

CMD ["/run.sh"]
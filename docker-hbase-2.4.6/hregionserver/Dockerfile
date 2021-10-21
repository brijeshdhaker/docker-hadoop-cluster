FROM brijeshdhaker/hbase-base:2.4.6

COPY docker-hbase-2.4.6/hregionserver/run.sh /run.sh
RUN chmod +x /run.sh

EXPOSE 16020 16030

CMD ["/run.sh"]
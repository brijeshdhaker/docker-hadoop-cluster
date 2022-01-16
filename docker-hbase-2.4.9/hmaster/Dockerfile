FROM brijeshdhaker/hbase-base:2.4.9

COPY docker-hbase-2.4.9/hmaster/run.sh /run.sh
RUN chmod +x /run.sh

EXPOSE 16000 16010

CMD ["/run.sh"]
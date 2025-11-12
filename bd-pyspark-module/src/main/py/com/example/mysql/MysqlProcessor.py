import mysql.connector


class MysqlProcessor(object):

    connection = None

    # The init method or constructor
    def __init__(self, uuid=None):
        self.connection = mysql.connector.connect(
            user="operate",
            password="paSSW0rd",
            host="mysqlserver",
            port="3306",
            database="SANDBOXDB",
            autocommit=False
        )
        print(self.connection)

    #
    def insert(self, sql = None, values = None):

        if not sql:
            raise Exception("sql string is empty.")

        if not isinstance(values, list):
            raise TypeError("Only list are allowed.")

        cursor = self.connection.cursor()
        cursor.executemany(sql, values)
        self.commit()
        return cursor.rowcount

    #
    def saveAndCommit(self, sql = None, value = None):
        cursor = self.connection.cursor()
        #sql = "INSERT INTO customers (name, address) VALUES (%s, %s)"
        #value = ("John", "Highway 21")
        cursor.execute(sql, value)
        self.commit()
        print(cursor.rowcount, "record inserted.")
    #
    def fetchAll(self, sql = None):
        cursor = self.connection.cursor()
        cursor.execute(sql)
        results = cursor.fetchall()
        return results

    #
    def execute(self, sql = None):
        cursor = self.connection.cursor()
        cursor.execute(sql)
        self.commit()
        return cursor.rowcount

    #
    def commit(self):
        # Disconnecting from the server
        self.connection.commit()

    #
    def close(self):
        # Disconnecting from the server
        self.connection.close()
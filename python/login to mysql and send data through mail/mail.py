#!/usr/bin/python

import smtplib
import MySQLdb
import sys

# open a database connection
# be sure to change the host IP address, username, password and database name to match your own
connection = MySQLdb.connect (host = "localhost", user = "huy1395", passwd = "password", db ="STEAM")
# prepare a cursor object using cursor() method
cursor = connection.cursor ()
# execute the SQL query using execute() method.
cursor.execute ("select * from users")
# fetch all of the rows from the query
data = cursor.fetchall ()
# print the rows
##    print row[0], row[1]
# close the cursor object
mail = smtplib.SMTP('smtp.gmail.com',587)

mail.ehlo()

mail.starttls()

mail.login('sender@gmail.com', 'QHuy1395')
sender = 'sender@gmail.com'
receivers = ['DoomLord1395@gmail.com']

message = """From: From Person <sender@gmail.com>
To: To Person <DoomLord1395@gmail.com>
Subject: '{0}' """ .format(data)

mail.sendmail(sender, receivers, message)

mail.close()

cursor.close ()
# close the connection
connection.close ()
# exit the program
sys.exit()

#try:
 #  smtpObj = smtplib.SMTP('localhost')
  # smtpObj.sendmail(sender, receivers, message)
   #print "Successfully sent email"
#except SMTPException:
 #  print "Error: unable to send email"

# import the MySQLdb and sys modules


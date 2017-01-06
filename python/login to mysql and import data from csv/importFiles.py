import xlrd
import MySQLdb

# Open the workbook and define the worksheet
book = xlrd.open_workbook("pytest.xls")
sheet = book.sheet_by_name("source")

# Establish a MySQL connection
database = MySQLdb.connect (host = "localhost", user = "huy1395", passwd = "password", db ="STEAM")

# Get the cursor, which is used to traverse the database, line by line
cursor = database.cursor()

# Create the INSERT INTO sql query
query = """INSERT INTO users (longitude, latitude, time, jam) VALUES (%s, %s, %s, %s)"""

# Create a For loop to iterate through each row in the XLS file, starting at row 2 to skip the headers
for r in range(1, sheet.nrows):
      longitude      = sheet.cell(r,).value
      latitude = sheet.cell(r,1).value
      time          = sheet.cell(r,2).value
      jam     = sheet.cell(r,3).value

      # Assign values from each row
      values = (longitude, latitude, time, jam)

      # Execute sql Query
      cursor.execute(query, values)

# Close the cursor
cursor.close()

# Commit the transaction
database.commit()

# Close the database connection
database.close()

# Print results
print ""
print "All Done! Bye, for now."
print ""
columns = str(sheet.ncols)
rows = str(sheet.nrows)
print "I just imported " '%s'" columns and " '%s' " rows to MySQL!" %columns %rows

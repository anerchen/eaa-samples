
## To start the database
##
## Database manipulation
##   buildr eaa:start_database
##   buildr eaa:stop_database
##   buildr eaa:delete_database

repositories.remote << 'http://repo1.maven.org/maven2'

SERVLET = 'javax.servlet:servlet-api:jar:2.5'
HSQLDB = 'org.hsqldb:hsqldb:jar:2.3.2'
SQLTOOL = 'org.hsqldb:sqltool:jar:2.3.2'

DATABASE_DIR = 'database'

def runSql(sqlFile)
  puts "Running SQL script: #{sqlFile}"
  cp = Buildr.artifacts([HSQLDB,SQLTOOL]).join(":")
  wd = Dir.getwd
  cmd = "java -cp #{cp} org.hsqldb.cmdline.SqlTool --rcFile=#{wd}/sqltool.rc --driver=org.hsqldb.jdbc.JDBCDriver --autoCommit --debug default #{sqlFile}"
  puts cmd
  system cmd
end

def getClasspath
  cp = Buildr.artifacts(SERVLET,HSQLDB)
  cp << 'target/classes'
  return cp.join(':')
end

define 'eaa' do
  project.group = 'com.mikehelmick'
  project.version = '1.0.0'
  manifest['Copyright'] = 'Mike Helmick (C) 2007, 2014'

  compile.with SERVLET, HSQLDB, SQLTOOL
  package :jar
  
  task :start_database do
    mkpath DATABASE_DIR
    # Classpath for HSQLDB
    cp = Buildr.artifacts(HSQLDB)
    puts "protip: background this task"
    system "java -cp #{cp} org.hsqldb.Server -database.0 database/db -dbname.0 xdb"
  end

  task :stop_database do
    runSql("#{Dir.getwd()}/sql/shutdown.sql")
  end
  
  task :delete_database do
    begin
      runSql("#{Dir.getwd()}/sql/shutdown.sql")
    rescue
    end
    remove_dir(DATABASE_DIR, true)
    puts "Database contents trashed."
  end
  
  task :ch09_ts => :compile do
    runSql('sql/chapter09_before.sql');
    system "java -cp #{getClasspath} eaa.chapter9.transactionscript.TransactionScript"
  end
  
  task :ch09_dm => :compile do
    system "java -cp #{getClasspath} eaa.chapter9.domainmodel.DomainModel"
  end
end
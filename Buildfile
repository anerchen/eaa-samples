
repositories.remote << 'http://repo1.maven.org/maven2'

SERVLET = 'javax.servlet:servlet-api:jar:2.5'
HSQLDB = 'org.hsqldb:hsqldb:jar:2.3.2'

define 'eaa-samples' do
  project.group = 'com.mikehelmick'
  project.version = '1.0.0'
  manifest['Copyright'] = 'Mike Helmick (C) 2007, 2014'

  compile.with SERVLET, HSQLDB
  package :jar  
end
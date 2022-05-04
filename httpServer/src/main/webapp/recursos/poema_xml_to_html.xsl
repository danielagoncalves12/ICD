<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" version="4.0" encoding="iso-8859-1" indent="yes"/>
  <xsl:template match="/poema">
    <html>
      <head>
        <meta http-equiv="Content-Type" content="text/html"/>
        <meta http-equiv="Content-Language" content="pt"/>
        <title>Poema</title>
        <link href="http://fonts.googleapis.com/css?family=Great+Vibes" rel="stylesheet" type="text/css"/>
      </head>
      <body style="font: 400 30px/0.8 'Great Vibes', Helvetica, sans-serif; color: #9a3324">
        <xsl:apply-templates select="título"/>
        <br/>
        <xsl:apply-templates select="estrofe"/>
        <xsl:apply-templates select="autor"/>
      </body>
    </html>
  </xsl:template>
  <xsl:template match="título">
    <span  style="font-family: 'Monotype Corsiva'">Título: </span>
    <span style="color:DarkGoldenRod">
      <xsl:value-of select="."/>
    </span>
    <br/>
  </xsl:template>
  <xsl:template match="autor">
    <span style="font-family: 'Monotype Corsiva'">Autor: </span>
    <span style="color:DarkGoldenRod">
      <xsl:value-of select="."/>
    </span>
    <br/>
  </xsl:template>
  <xsl:template match="estrofe">
    <xsl:apply-templates select="verso"/>
    <br/>
  </xsl:template>
  <xsl:template match="verso">
    <span style="color:#003300">
      <xsl:value-of select="."/>
    </span>
    <br/>
  </xsl:template>
</xsl:stylesheet>



To insert the billClock-applet paste these lines to your page:


        <applet code="billsClock.class" width=100 height=100 ALIGN=MIDDLE>
	    <param name=BGCOLOR value="DDDDEE">
	    <param name=FACECOLOR value="FFFFFF">
	    <param name=SWEEPCOLOR value="FF0000">
	    <param name=MINUTECOLOR value="009090">
	    <param name=HOURCOLOR value="000000">
	    <param name=TEXTCOLOR value="000000">
	    <param name=CASECOLOR value="CCCCCC">
	    <param name=TRIMCOLOR value="C0C0C0">
	    <param name=LOGOIMAGEURL value="java.gif">
	    <param name=LOCALONLY value=1>
	    </applet>




***************************************************************************************
                         TWEEKING THE PARAMETERS 
***************************************************************************************


You can change the color of these parameters:
	    <param name=BGCOLOR value="DDDDEE"> (The area outside the clock)
	    <param name=FACECOLOR value="FFFFFF"> (The clock background)
	    <param name=SWEEPCOLOR value="FF0000"> (Seconds)
	    <param name=MINUTECOLOR value="009090"> (Minutes)
	    <param name=HOURCOLOR value="000000"> (Hours)
	    <param name=TEXTCOLOR value="000000"> (Text along the outline of the clock)
	    <param name=CASECOLOR value="CCCCCC"> (Outline of the clock)
	    <param name=TRIMCOLOR value="FFC0FF"> (Outline thin border)

You can enter a small image to be used as the icon on the clock:
	    <param name=LOGOIMAGEURL value="java.gif">

You can tell whether you want the time to be local or GMT:
	    <param name=LOCALONLY value=1> (0=GTM, 1=Local time on visitors computer)


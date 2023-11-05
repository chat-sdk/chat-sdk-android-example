
-keep public class org.bouncycastle.jcajce.provider.**, org.jivesoftware.**, app.xmpp.**, sdk.chat.**, firestream.chat.**, sdk.guru.**, co.chatsdk.** {
    public protected *;
}
-keep class **.R$* {
    <fields>;
}

-keep public class smartadapter.**, materialsearchview.**, org.ocpsoft.prettytime.** {
    public protected *;
}

-keepnames class org.kxml2.io.**, org.xmlpull.** {
    public protected *;
}


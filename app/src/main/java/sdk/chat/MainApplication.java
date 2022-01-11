package sdk.chat;

import android.app.Application;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.xmpp.adapter.module.XMPPModule;
import io.reactivex.disposables.Disposable;
import sdk.chat.android.live.R;
import sdk.chat.core.hook.Executor;
import sdk.chat.core.hook.Hook;
import sdk.chat.core.hook.HookEvent;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.utils.Device;
import sdk.chat.firebase.adapter.module.FirebaseModule;
import sdk.chat.firebase.push.FirebasePushModule;
import sdk.chat.firebase.upload.FirebaseUploadModule;
import sdk.chat.message.location.LocationMessageModule;
import sdk.chat.ui.extras.ExtrasModule;
import sdk.chat.ui.module.UIModule;

/**
 * Created by Ben Smiley on 6/8/2014.
 */
//public class MainApplication extends MultiDexApplication {
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            xmpp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xmpp() {
        try {

            ChatSDK.builder()

                    // Configure the library
                    .setGoogleMaps("AIzaSyCwwtZrlY9Rl8paM0R6iDNBEit_iexQ1aE")
                    .setAnonymousLoginEnabled(false)
                    .setDebugModeEnabled(false)
                    .setThreadDestructionEnabled(false)
                    .setClientPushEnabled(true)
                    .setAllowUserToRejoinGroup(true)

//                    .setDebugUsername("user2")
//                    .setDebugPassword("testpw00")

                    .build()

                    // Add modules to handle file uploads, push notifications
                    .addModule(FirebaseUploadModule.shared())
                    .addModule(FirebasePushModule.shared())

                    .addModule(XMPPModule.builder()
//                            .setXMPP("we-connect-dev.com", "we-connect-dev.com")
                            .setXMPP("xmpp.app", "xmpp.app")
                            .setAllowServerConfiguration(false)
//                            .setSecurityMode("required")
//                            .setSecurityMode("ifpossible")
//                            .setSecurityMode("ifpossible")
                            .setPingInterval(5)
                            .setDebugEnabled(true)
                            .setSubscriptionMode(Roster.SubscriptionMode.manual)
                            .setReciprocalPresenceRequest(false)
                            .setOnlinePresenceModes(new ArrayList<Presence.Mode>() {{
                                add(Presence.Mode.available);
                                add(Presence.Mode.chat);
                                add(Presence.Mode.away);
                                add(Presence.Mode.dnd);
                            }})
//                            .setSecurityMode("ifpossible")
                            .build())

//                    .addModule(AudioMessageModule.shared())
                    .addModule(LocationMessageModule.shared())

                    .addModule(UIModule.builder()
                            .setRequestPermissionsOnStartup(false)
                            .setMessageSelectionEnabled(true)
                            .setUsernameHint("JID")
                            .setMessageForwardingEnabled(true)
                            .setMessageReplyEnabled(true)
                            .setResetPasswordEnabled(false)
                            .setPublicRoomCreationEnabled(true)
                            .setPublicRoomsEnabled(false)
                            .build())

//                    .addModule(XMPPReadReceiptsModule.shared())
                    .addModule(ExtrasModule.builder()
                            .setQrCodesEnabled(true)
                            .setDrawerEnabled(false)
                            .build())

                    .build().activateWithEmail(this, "ben@sdk.chat");

//            ChatSDK.config().setDebugUsername(Device.honor() ? "a3": "a4");
//            ChatSDK.config().setDebugPassword("123");

//            ChatSDK.ui().setThreadDetailsActivity(ThreadDetailsActivity.class);

//            chatsdkAuth(Device.honor() ? "xxx1" : "xxx2", "123", "test@conference.xmpp.app");


        }
        catch (Exception e) {
            e.printStackTrace();
            Logger.error(e.getLocalizedMessage());
            assert(false);
        }



//        Disposable d = ChatSDK.events().sourceOnMain().subscribe(networkEvent -> {
//            networkEvent.debug();
//        });
//
//        d = ChatSDK.events().errorSourceOnMain().subscribe(throwable -> {
//            // Catch errors
//            throwable.printStackTrace();
//        });
//
//        ChatSDK.hook().addHook(Hook.sync(data -> {
//            Object message = data.get(HookEvent.Message);
//            if (message instanceof Message) {
//                Logger.info(message);
//            }
//        }), HookEvent.MessageReceived);
//
//        ChatSDK.hook().addHook(Hook.sync(data -> {
//            Object entryObject = data.get(XMPPManager.xmppRosterEntry);
//            if (entryObject instanceof RosterEntry) {
//                RosterEntry entry = (RosterEntry) entryObject;
//                Logger.debug(entry);
//            }
//        }), XMPPManager.xmppRosterItemUpdated, XMPPManager.xmppRosterItemAdded, XMPPManager.xmppRosterItemRemoved);
//
//        Disposable dd = ChatSDK.events().sourceOnMain().filter(NetworkEvent.filterType(EventType.UserPresenceUpdated)).subscribe(networkEvent -> {
//            User user = networkEvent.getUser();
//            String availability = user.getAvailability();
//            if (user.getIsOnline()) {
//                Logger.debug("Online");
//            } else {
//                Logger.debug("Offline");
//            }
//        });
    }

    public void firebase() throws Exception {
        String rootPath = "pre_2";

        ChatSDK.builder()
                .setGoogleMaps("AIzaSyCwwtZrlY9Rl8paM0R6iDNBEit_iexQ1aE")
                .setAnonymousLoginEnabled(false)

//                .setDebugModeEnabled(true)
                .setRemoteConfigEnabled(false)
                .setPublicChatRoomLifetimeMinutes(TimeUnit.HOURS.toMinutes(24))
                .setSendSystemMessageWhenRoleChanges(true)
                .setRemoteConfigEnabled(true)
                .build()

                // Add the network adapter module
                .addModule(
                        FirebaseModule.builder()
                                .setFirebaseRootPath(rootPath)
                                .setDisableClientProfileUpdate(false)
                                .setDevelopmentModeEnabled(true)
                                .build()
                )

                // Add the UI module
                .addModule(UIModule.builder()
                        .setPublicRoomCreationEnabled(true)
                        .setPublicRoomsEnabled(true)
                        .setTheme(R.style.GGTheme)
                        .build()
                )

                // Add modules to handle file uploads, push notifications
//                .addModule(FirebaseUploadModule.shared())
//                .addModule(FirebasePushModule.shared())
//                .addModule(ProfilePicturesModule.shared())
//                .addModule()

                .addModule(ExtrasModule.builder(config -> {
                    if (Device.honor(this)) {
                        config.setDrawerEnabled(false);
                    }
                }))



//                .addModule(FirebaseUIModule.builder()
//                        .setProviders(EmailAuthProvider.PROVIDER_ID, PhoneAuthProvider.PROVIDER_ID)
//                        .build()
//                )

                // Activate
                .build()
                .activate(this);

//        Fire.stream().isInitialized();


        Disposable d = ChatSDK.events().sourceOnMain().subscribe(networkEvent -> {
            networkEvent.debug();
        });

        d = ChatSDK.events().errorSourceOnMain().subscribe(t -> {
            t.printStackTrace();
        });

        ChatSDK.hook().addHook(Hook.sync(hashMap -> {
//            ChatSDK.currentUser().setMetaString("Test", "Data");
//            ChatSDK.core().pushUser().subscribe();
            System.out.println("");
        }), HookEvent.DidAuthenticate);


    }



}

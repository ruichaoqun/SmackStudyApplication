package com.smack.administrator.smackstudyapplication;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.EntityBareJid;

/**
 * <p>Description.</p>
 * <p>
 * <b>Maintenance History</b>:
 * <table>
 * <tr>
 * <th>Date</th>
 * <th>Developer</th>
 * <th>Target</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td>2018-10-18 11:36</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class XmppVCardManager {
    private VCardManager vCardManager;

    public void getUserAvatar(byte[] bytes, EntityBareJid bareJid) throws XMPPException.XMPPErrorException, SmackException.NotConnectedException, InterruptedException, SmackException.NoResponseException {
        VCard vCard = VCardManager.getInstanceFor(XmppConnection.getInstance().getConnection()).loadVCard(bareJid);
        vCard.setAvatar(bytes);
        VCardManager.getInstanceFor(XmppConnection.getInstance().getConnection()).saveVCard(vCard);
    }
}

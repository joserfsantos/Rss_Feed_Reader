package com.bluecatpixel.rssfeedreader.xmlparser;

import com.bluecatpixel.rssfeedreader.model.Channel;
import com.bluecatpixel.rssfeedreader.model.Image;
import com.bluecatpixel.rssfeedreader.model.Item;
import com.bluecatpixel.rssfeedreader.model.Thumbnail;
import com.bluecatpixel.rssfeedreader.utils.Utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_ATOM_LINK;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_CHANNEL;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_COPYRIGHT;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_DESCRIPTION;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_HEIGHT;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_IMAGE;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_ITEM;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_LANGUAGE;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_LAST_BUILD_DATE;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_LINK;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_MEDIA_THUMBNAIL;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_PUB_DATE;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_TITLE;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_TTL;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_URL;
import static com.bluecatpixel.rssfeedreader.xmlparser.XmlTokens.TOKEN_WIDTH;

public class RssFeedHandler extends DefaultHandler {

    private Channel mChannel;

    private boolean inChannel;
    private boolean inChannelTitle;
    private boolean inChannelLink;
    private boolean inChannelDescription;
    private boolean inChannelLanguage;
    private boolean inChannelLastBuildDate;
    private boolean inChannelCopyright;
    private boolean inChannelImage;
    private boolean inChannelTtl;
    private boolean inImageUrl;
    private boolean inImageTitle;
    private boolean inImageLink;
    private boolean inImageWidth;
    private boolean inImageHeight;
    private boolean inChannelItem;
    private boolean inItemTitle;
    private boolean inItemDescription;
    private boolean inItemLink;
    private boolean inItemPubDate;

    private Item itemAux;
    private Thumbnail thumbnailAux;
    private Image imageAux;
    private StringBuilder sbAux;

    public RssFeedHandler() {
        this.mChannel = new Channel();
    }

    public Channel getChannel() {
        return mChannel;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (inChannel) {
            if (!inChannelImage && localName.equals(TOKEN_IMAGE)) {
                inChannelImage = true;
                imageAux = new Image();
            } else if (inChannelImage) {
                if (localName.equals(TOKEN_URL)) {
                    sbAux = new StringBuilder();
                    inImageUrl = true;
                } else if (localName.equals(TOKEN_TITLE)) {
                    sbAux = new StringBuilder();
                    inImageTitle = true;
                } else if (localName.equals(TOKEN_LINK)) {
                    sbAux = new StringBuilder();
                    inImageLink = true;
                } else if (localName.equals(TOKEN_WIDTH)) {
                    sbAux = new StringBuilder();
                    inImageWidth = true;
                } else if (localName.equals(TOKEN_HEIGHT)) {
                    sbAux = new StringBuilder();
                    inImageHeight = true;
                }
            } else if (!inChannelItem && localName.equals(TOKEN_ITEM)) {
                inChannelItem = true;
                itemAux = new Item();
            } else if (inChannelItem) {
                if (qName.equals(TOKEN_MEDIA_THUMBNAIL)) {
                    thumbnailAux = new Thumbnail();
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (attributes.getQName(i).equals(TOKEN_WIDTH)) {
                            thumbnailAux.setWidth(Utils.parseToInt(attributes.getValue(i)));
                        } else if (attributes.getQName(i).equals(TOKEN_HEIGHT)) {
                            thumbnailAux.setHeight(Utils.parseToInt(attributes.getValue(i)));
                        } else if (attributes.getQName(i).equals(TOKEN_URL)) {
                            thumbnailAux.setUrl(attributes.getValue(i));
                        }
                    }
                    itemAux.getThumbnails().add(thumbnailAux);
                } else if (localName.equals(TOKEN_TITLE)) {
                    sbAux = new StringBuilder();
                    inItemTitle = true;
                } else if (localName.equals(TOKEN_DESCRIPTION)) {
                    sbAux = new StringBuilder();
                    inItemDescription = true;
                } else if (localName.equals(TOKEN_LINK) && !qName.equals(TOKEN_ATOM_LINK)) {
                    sbAux = new StringBuilder();
                    inItemLink = true;
                } else if (localName.equals(TOKEN_PUB_DATE)) {
                    sbAux = new StringBuilder();
                    inItemPubDate = true;
                }
            } else if (localName.equals(TOKEN_TTL)) {
                sbAux = new StringBuilder();
                inChannelTtl = true;
            } else if (localName.equals(TOKEN_TITLE)) {
                sbAux = new StringBuilder();
                inChannelTitle = true;
            } else if (localName.equals(TOKEN_LINK)) {
                sbAux = new StringBuilder();
                inChannelLink = true;
            } else if (localName.equals(TOKEN_DESCRIPTION)) {
                sbAux = new StringBuilder();
                inChannelDescription = true;
            } else if (localName.equals(TOKEN_LANGUAGE)) {
                sbAux = new StringBuilder();
                inChannelLanguage = true;
            } else if (localName.equals(TOKEN_LAST_BUILD_DATE)) {
                sbAux = new StringBuilder();
                inChannelLastBuildDate = true;
            } else if (localName.equals(TOKEN_COPYRIGHT)) {
                sbAux = new StringBuilder();
                inChannelCopyright = true;
            }
        } else if (localName.equals(TOKEN_CHANNEL)) {
            mChannel = new Channel();
            inChannel = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);

        if (inChannel) {
            if (inChannelTitle) {
                sbAux.append(new String(ch, start, length));
            } else if (inChannelLink) {
                sbAux.append(new String(ch, start, length));
            } else if (inChannelDescription) {
                sbAux.append(new String(ch, start, length));
            } else if (inChannelLanguage) {
                sbAux.append(new String(ch, start, length));
            } else if (inChannelLastBuildDate) {
                sbAux.append(new String(ch, start, length));
            } else if (inChannelCopyright) {
                sbAux.append(new String(ch, start, length));
                mChannel.setCopyright(new String(ch, start, length));
            } else if (inChannelTtl) {
                sbAux.append(new String(ch, start, length));
            } else if (inChannelImage) {
                if (inImageUrl) {
                    sbAux.append(new String(ch, start, length));
                } else if (inImageTitle) {
                    sbAux.append(new String(ch, start, length));
                } else if (inImageLink) {
                    sbAux.append(new String(ch, start, length));
                } else if (inImageWidth) {
                    sbAux.append(new String(ch, start, length));
                } else if (inImageHeight) {
                    sbAux.append(new String(ch, start, length));
                }

            } else if (inChannelItem) {
                if (inItemTitle) {
                    sbAux.append(new String(ch, start, length));
                } else if (inItemDescription) {
                    sbAux.append(new String(ch, start, length));
                } else if (inItemLink) {
                    sbAux.append(new String(ch, start, length));
                } else if (inItemPubDate) {
                    sbAux.append(new String(ch, start, length));
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (inChannel) {
            if (inChannelImage && localName.equals(TOKEN_IMAGE)) {
                inChannelImage = false;
                mChannel.setImage(imageAux);
            } else if (inChannelImage) {
                if (localName.equals(TOKEN_URL)) {
                    inImageUrl = false;
                    imageAux.setUrl(sbAux.toString());
                } else if (localName.equals(TOKEN_TITLE)) {
                    inImageTitle = false;
                    imageAux.setTitle(sbAux.toString());
                } else if (localName.equals(TOKEN_LINK)) {
                    inImageLink = false;
                    imageAux.setLink(sbAux.toString());
                } else if (localName.equals(TOKEN_WIDTH)) {
                    inImageWidth = false;
                    imageAux.setWidth(Utils.parseToInt(sbAux.toString()));
                } else if (localName.equals(TOKEN_HEIGHT)) {
                    inImageHeight = false;
                    imageAux.setHeight(Utils.parseToInt(sbAux.toString()));
                }
            } else if (inChannelItem && localName.equals(TOKEN_ITEM)) {
                inChannelItem = false;
                mChannel.getItems().add(itemAux);
            } else if (inChannelItem) {
                if (localName.equals(TOKEN_TITLE)) {
                    inItemTitle = false;
                    itemAux.setTitle(sbAux.toString());
                } else if (localName.equals(TOKEN_DESCRIPTION)) {
                    inItemDescription = false;
                    itemAux.setDescription(sbAux.toString());
                } else if (localName.equals(TOKEN_LINK)) {
                    inItemLink = false;
                    itemAux.setLink(sbAux.toString());
                } else if (localName.equals(TOKEN_PUB_DATE)) {
                    inItemPubDate = false;
                    itemAux.setPubDate(sbAux.toString());
                }
            } else if (localName.equals(TOKEN_TTL)) {
                inChannelTtl = false;
                mChannel.setTtl(Utils.parseToInt(sbAux.toString()));
            } else if (localName.equals(TOKEN_TITLE)) {
                inChannelTitle = false;
                mChannel.setTitle(sbAux.toString());
            } else if (localName.equals(TOKEN_LINK) && !qName.equals(TOKEN_ATOM_LINK)) {
                inChannelLink = false;
                mChannel.setLink(sbAux.toString());
            } else if (localName.equals(TOKEN_DESCRIPTION)) {
                inChannelDescription = false;
                mChannel.setDescription(sbAux.toString());
            } else if (localName.equals(TOKEN_LANGUAGE)) {
                inChannelLanguage = false;
                mChannel.setLanguage(sbAux.toString());
            } else if (localName.equals(TOKEN_LAST_BUILD_DATE)) {
                inChannelLastBuildDate = false;
                mChannel.setLastBuildDate(sbAux.toString());
            } else if (localName.equals(TOKEN_COPYRIGHT)) {
                inChannelCopyright = false;
                mChannel.setCopyright(sbAux.toString());
            }
        } else if (localName.equals(TOKEN_CHANNEL)) {
            mChannel = new Channel();
            inChannel = false;
        }
    }


}

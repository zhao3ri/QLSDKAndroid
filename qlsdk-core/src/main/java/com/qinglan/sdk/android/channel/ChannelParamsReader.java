package com.qinglan.sdk.android.channel;

import android.text.TextUtils;

import com.qinglan.sdk.android.common.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public final class ChannelParamsReader {
    private InputStream inputStream;
    private OnReadEndListener mListener;

    public ChannelParamsReader(InputStream is) {
        inputStream = is;
    }

    public void parser() {
        Log.d("parser channel");
        try {
            parserXml();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(e.toString());
        }
    }

    public void setOnReadEndListener(OnReadEndListener listener) {
        mListener = listener;
    }

    private void parserXml() throws ParserConfigurationException, SAXException, IOException {
        if (inputStream != null) {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(inputStream, new XmlParserHandler());
        }
    }

    private class XmlParserHandler extends DefaultHandler {
        private String curTag;
        private ChannelParam channel;
        private Map<Integer, ChannelParam> channels;

        private static final String ELEMENT_CHANNEL = "channel";
        private static final String ATTRIBUTE_CHANNEL_ID = "id";
        private static final String ATTRIBUTE_CHANNEL_NAME = "name";
        private static final String ELEMENT_CLASS = "class-name";

        @Override
        public void startDocument() throws SAXException {
            channels = new HashMap<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals(ELEMENT_CHANNEL)) {
                channel = new ChannelParam();
                if (attributes != null) {
                    Log.d("attributes length===" + attributes.getLength());
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (attributes.getQName(i).equals(ATTRIBUTE_CHANNEL_ID)) {
                            channel.id = Integer.parseInt(attributes.getValue(ATTRIBUTE_CHANNEL_ID));
                        } else if (attributes.getQName(i).equals(ATTRIBUTE_CHANNEL_NAME)) {
                            channel.name = attributes.getValue(ATTRIBUTE_CHANNEL_NAME);
                        }
                    }
                }
            }
            curTag = qName;
            Log.d("start element:" + qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String data = new String(ch, start, length);
            if (channel != null && !TextUtils.isEmpty(curTag)) {
                if (curTag.equals(ELEMENT_CLASS)) {
                    channel.clazz = data;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals(ELEMENT_CHANNEL) && channel != null && !channel.isEmpty()) {
                if (!channels.containsKey(channel.id)) {
                    channels.put(channel.id, channel);
                }
                channel = null;
            }
            curTag = null;
            Log.d("end element:" + qName);
        }

        @Override
        public void endDocument() throws SAXException {
            if (mListener != null) {
                mListener.onEnd(channels);
            }
        }
    }

    public class ChannelParam {
        int id;
        String name;
        String clazz;

        boolean isEmpty() {
            return id == 0 && TextUtils.isEmpty(name) && TextUtils.isEmpty(clazz);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getClazz() {
            return clazz;
        }
    }

    public interface OnReadEndListener {
        void onEnd(Map<Integer, ChannelParam> p);
    }
}

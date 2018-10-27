package com.qinglan.sdk.android.platform;

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

public final class PlatformParamsReader {
    private InputStream inputStream;
    private OnReadEndListener mListener;

    public PlatformParamsReader(InputStream is) {
        inputStream = is;
    }

    public void parser() {
        Log.d("parser platform");
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
        private PlatformParam platform;
        private Map<Integer, PlatformParam> platforms;

        private static final String ELEMENT_PLATFORM = "platform";
        private static final String ATTRIBUTE_PLATFORM_ID = "id";
        private static final String ATTRIBUTE_PLATFORM_NAME = "name";
        private static final String ELEMENT_CLASS = "class-name";

        @Override
        public void startDocument() throws SAXException {
            platforms = new HashMap<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals(ELEMENT_PLATFORM)) {
                platform = new PlatformParam();
                if (attributes != null) {
                    if (attributes.getIndex(ATTRIBUTE_PLATFORM_ID) != -1) {
                        platform.id = Integer.parseInt(attributes.getValue(ATTRIBUTE_PLATFORM_ID));
                    } else if (attributes.getIndex(ATTRIBUTE_PLATFORM_NAME) != -1) {
                        platform.name = attributes.getValue(ATTRIBUTE_PLATFORM_NAME);
                    }
                }
            }
            curTag = qName;
            Log.d("start element:" + qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String data = new String(ch, start, length);
            if (platform != null && !TextUtils.isEmpty(curTag)) {
                if (curTag.equals(ELEMENT_CLASS)) {
                    platform.clazz = data;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals(ELEMENT_PLATFORM) && platform != null && !platform.isEmpty()) {
                if (!platforms.containsKey(platform.id)) {
                    platforms.put(platform.id, platform);
                }
                platform = null;
            }
            curTag = null;
            Log.d("end element:" + qName);
        }

        @Override
        public void endDocument() throws SAXException {
            if (mListener != null) {
                mListener.onEnd(platforms);
            }
        }
    }

    public class PlatformParam {
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
        void onEnd(Map<Integer, PlatformParam> p);
    }
}

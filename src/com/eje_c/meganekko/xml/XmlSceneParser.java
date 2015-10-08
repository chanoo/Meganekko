/* Copyright 2015 eje inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eje_c.meganekko.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.eje_c.meganekko.Camera;
import com.eje_c.meganekko.VrContext;
import com.eje_c.meganekko.Scene;
import com.eje_c.meganekko.SceneObject;

import android.util.Xml;

public class XmlSceneParser {

    private final VrContext mContext;
    private final XmlSceneObjectParser mObjectParser;

    public XmlSceneParser(VrContext context) {
        this.mContext = context;
        this.mObjectParser = new XmlSceneObjectParser(context);
    }

    /**
     * Parse scene from XML resource.
     * 
     * @param xmlRes
     *            XML resource.
     * @param scene
     *            Root scene. It can be null.
     * @return Parsed {@code Scene}.
     * @throws XmlPullParserException
     * @throws IOException
     */
    public Scene parse(int xmlRes, Scene scene) throws XmlPullParserException, IOException {
        return parse(mContext.getContext().getResources().getXml(xmlRes), scene);
    }

    /**
     * Parse scene from {@code URL}. XML can be loaded any where.
     * 
     * @param url
     *            URL pointing to XML resource.
     * @param scene
     *            Root scene. It can be null.
     * @return Parsed {@code Scene}.
     * @throws XmlPullParserException
     * @throws IOException
     */
    public Scene parse(String url, Scene scene) throws XmlPullParserException, IOException {
        return parse(new URL(url).openStream(), scene);
    }

    /**
     * Parse scene from {@code InputStream}. XML can be loaded any where.
     * 
     * @param in
     *            {@code InputStream} of XML.
     * @param scene
     *            Root scene. It can be null.
     * @return Parsed {@code Scene}.
     * @throws XmlPullParserException
     * @throws IOException
     */
    public Scene parse(InputStream in, Scene scene) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return parse(parser, scene);
        } finally {
            in.close();
        }
    }

    /**
     * Parse scene from {@code XmlPullParser}. This method can be used with
     * {@code Resources#getXml(int)}.
     * 
     * @param parser
     * @param scene
     *            Root scene. It can be null.
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public Scene parse(XmlPullParser parser, Scene scene) throws XmlPullParserException, IOException {

        if (scene == null) {
            scene = new Scene(mContext);
        }

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            switch (parser.getEventType()) {

            case XmlPullParser.START_TAG:

                if ("camera".equals(parser.getName())) {

                    Camera camera = scene.getMainCamera();

                    while (parser.nextTag() == XmlPullParser.START_TAG) {
                        SceneObject object = mObjectParser.parse(parser);
                        if (object != null) {
                            camera.addChildObject(object);
                        }
                    }
                } else {

                    SceneObject object = mObjectParser.parse(parser);

                    if (object != null) {
                        scene.addSceneObject(object);
                    }
                }
                break;

            case XmlPullParser.END_TAG:
                break;
            }
        }

        return scene;
    }

    /**
     * Set true to use asynchronous loading. If set false, textures are loaded
     * synchronically. Default is true.
     * 
     * @param useAsyncLoading
     */
    public void setAsyncTextureLoading(boolean useAsyncLoading) {
        mObjectParser.setAsyncTextureLoading(useAsyncLoading);
    }
}

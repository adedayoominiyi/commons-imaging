/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;

public class PpmFileInfo extends FileInfo {
    private final int max;
    private final float scale;
    private final int bytesPerSample; 

    public PpmFileInfo(int width, int height, boolean RAWBITS, int max) throws ImageReadException {
        super(width, height, RAWBITS);
        if (max <= 0) {
            throw new ImageReadException("PGM maxVal " + max
                    + " is out of range [1;65535]");
        } else if (max <= 255) {
            scale = 255f;
            bytesPerSample = 1;
        } else if (max <= 65535) {
            scale = 65535f;
            bytesPerSample = 2;
        } else {
            throw new ImageReadException("PGM maxVal " + max
                    + " is out of range [1;65535]");
        }
        this.max = max;
    }

    @Override
    public int getNumComponents() {
        return 3;
    }

    @Override
    public int getBitDepth() {
        return 8;
    }

    @Override
    public ImageFormat getImageType() {
        return ImageFormat.IMAGE_FORMAT_PGM;
    }

    @Override
    public String getImageTypeDescription() {
        return "PGM: portable graymap file    format";
    }

    @Override
    public String getMIMEType() {
        return "image/x-portable-graymap";
    }

    @Override
    public int getColorType() {
        return ImageInfo.COLOR_TYPE_GRAYSCALE;
    }

    @Override
    public int getRGB(InputStream is) throws IOException {
        int red = readSample(is, bytesPerSample);
        int green = readSample(is, bytesPerSample);
        int blue = readSample(is, bytesPerSample);

        red = scaleSample(red, scale, max);
        green = scaleSample(green, scale, max);
        blue = scaleSample(blue, scale, max);
        int alpha = 0xff;

        int rgb = ((0xff & alpha) << 24) | ((0xff & red) << 16)
                | ((0xff & green) << 8) | ((0xff & blue) << 0);

        return rgb;
    }

    @Override
    public int getRGB(WhiteSpaceReader wsr) throws IOException {
        int red = Integer.parseInt(wsr.readtoWhiteSpace());
        int green = Integer.parseInt(wsr.readtoWhiteSpace());
        int blue = Integer.parseInt(wsr.readtoWhiteSpace());

        red = scaleSample(red, scale, max);
        green = scaleSample(green, scale, max);
        blue = scaleSample(blue, scale, max);
        int alpha = 0xff;

        int rgb = ((0xff & alpha) << 24) | ((0xff & red) << 16)
                | ((0xff & green) << 8) | ((0xff & blue) << 0);

        return rgb;
    }

    @Override
    public void dump() {
        // System.out.println("count: " + count);
    }
}

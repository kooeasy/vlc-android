/*****************************************************************************
 * MediaList.java
 *****************************************************************************
 * Copyright © 2013 VLC authors and VideoLAN
 * Copyright © 2013 Edward Wang
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/
package org.videolan.libvlc;

/**
 * Java/JNI wrapper for the libvlc_media_list_t structure.
 */
public class MediaList {
    private static final String TAG = "VLC/LibVLC/MediaList";

    private long mMediaListInstance = 0; // Read-only, reserved for JNI
    private LibVLC mLibVLC; // Used to create new objects that require a libvlc instance
    private boolean destroyed = false;

    public MediaList(LibVLC libVLC) {
        mMediaListInstance = init(libVLC);
        mLibVLC = libVLC;
    }
    private native long init(LibVLC libvlc_instance);

    @Override
    public void finalize() {
        if(!destroyed) destroy();
    }

    /**
     * Releases the media list.
     *
     * The object should be considered released after this and must not be used.
     */
    public void destroy() {
        nativeDestroy();
        mMediaListInstance = 0;
        mLibVLC = null;
        destroyed = true;
    }
    private native void nativeDestroy();

    public void add(String mrl) {
        add(mLibVLC, mrl);
    }
    private native void add(LibVLC libvlc_instance, String mrl);

    public void insert(int position, String mrl) {
        insert(mLibVLC, position, mrl);
    }
    private native void insert(LibVLC libvlc_instance, int position, String mrl);

    public native void remove(int position);

    public native int size();

    /**
     * @param position The index of the media in the list
     * @return null if not found
     */
    public native String getMRL(int position);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LibVLC Media List: {");
        for(int i = 0; i < size(); i++) {
            sb.append(((Integer)i).toString());
            sb.append(": ");
            sb.append(getMRL(i));
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
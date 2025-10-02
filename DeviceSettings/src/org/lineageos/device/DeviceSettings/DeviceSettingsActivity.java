/*
 * Copyright (C) 2018-2024 crDroid Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.device.DeviceSettings;

import android.os.Bundle;
import android.view.View;

import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class DeviceSettingsActivity extends CollapsingToolbarBaseActivity {
    private View bannerFadeOverlay;
    private boolean pinned = false; // Set true to pin the fade overlay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(" ");

        // Load your fragment as usual
        getSupportFragmentManager().beginTransaction().replace(
            R.id.content_frame,
            new DeviceSettings()).commit();

        // Inject banner dynamically into CollapsingToolbarLayout
        CollapsingToolbarLayout collapsingToolbar =
            findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbar != null) {
            View banner = getLayoutInflater().inflate(R.layout.banner_collapsing_toolbar, collapsingToolbar, false);

            // You may want to insert at position 0 to ensure it's on top
            collapsingToolbar.addView(banner, 0);

            bannerFadeOverlay = banner.findViewById(R.id.bannerFadeOverlay);

            // Animate fade overlay on scroll
            AppBarLayout appBar = findViewById(R.id.app_bar);
            if (appBar != null) {
                appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                    if (bannerFadeOverlay == null) return;
                    int totalScrollRange = appBarLayout.getTotalScrollRange();
                    float offsetFraction = Math.abs(verticalOffset) / (float) totalScrollRange;
                    float maxAlpha = 0.8f;
                    float alpha = pinned ? maxAlpha : maxAlpha * (1 - offsetFraction);
                    bannerFadeOverlay.setAlpha(alpha);
                });
            }
        }
    }
}

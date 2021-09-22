package com.alansolidum.veritogp;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.DateFilter;
import com.google.photos.library.v1.proto.Filters;
import com.google.photos.types.proto.DateRange;
import com.google.photos.types.proto.MediaItem;
import com.google.type.Date;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class VeritoAPI {
    private static final List<String> SCOPES = java.util.List.of(
            "https://www.googleapis.com/auth/photoslibrary.readonly");
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final java.io.File DATA_STORE_DIR = new java.io.File("./tokens_test");
    private static final int LOCAL_RECEIVER_PORT = 8888;

    private VeritoAPI() {
        throw new IllegalStateException("Utility class");
    }

    public static PhotosLibraryClient createClient(String credentialsPath, List<String> selectedScopes) throws IOException, GeneralSecurityException {
        PhotosLibrarySettings settings =
                PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(
                                getUserCredentials(credentialsPath, selectedScopes)))
                        .build();

        return PhotosLibraryClient.initialize(settings);
    }

    private static Credentials getUserCredentials(String credentialsPath, List<String> selectedScopes) throws IOException, GeneralSecurityException {
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        JSON_FACTORY, new InputStreamReader(new FileInputStream(credentialsPath)));
        String clientId = clientSecrets.getDetails().getClientId();
        String clientSecret = clientSecrets.getDetails().getClientSecret();

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JSON_FACTORY,
                        clientSecrets,
                        selectedScopes)
                        .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
                        .setAccessType("offline")
                        .build();

        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder().setPort(LOCAL_RECEIVER_PORT).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(credential.getRefreshToken())
                .build();
    }

    public static Filters setupDateRangeFilters(Date startDate, Date endDate) {
        DateRange dateRange = DateRange.newBuilder()
                .setStartDate(startDate)
                .setEndDate(endDate)
                .build();
        DateFilter dateFilter = DateFilter.newBuilder()
                .addRanges(dateRange)
                .build();

        return Filters.newBuilder()
                .setDateFilter(dateFilter)
                .build();
    }

    public static Set<String> getPhotolistByRange(Date startDate, Date endDate) {
        Set<String> files = new HashSet<>();

        try {
            PhotosLibraryClient client = VeritoAPI.createClient("./creds.json", SCOPES);

            Filters filters = setupDateRangeFilters(startDate, endDate);
            InternalPhotosLibraryClient.SearchMediaItemsPagedResponse response = client.searchMediaItems(filters);

            for (InternalPhotosLibraryClient.SearchMediaItemsPage page : response.iteratePages()) {
                for (MediaItem item : page.getValues()) {
                    files.add(item.getFilename());
                }
            }
        } catch (IOException|GeneralSecurityException e) {
            e.printStackTrace();
        }

        return files;
    }
}

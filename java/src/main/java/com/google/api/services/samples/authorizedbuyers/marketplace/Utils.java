/*
 * Copyright (c) 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.authorizedbuyers.marketplace;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplaceScopes;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/** Utilities used by the Authorized Buyers Marketplace API samples. */
public class Utils {
  /**
   * Specify the name of your application. If the application name is {@code null} or blank, the
   * application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "";

  /** Full path to JSON Key file - include file name */
  private static final java.io.File JSON_FILE = new java.io.File("INSERT_PATH_TO_JSON_FILE");

  /**
   * Global instance of a DateTimeFormatter used to parse LocalDate instances and convert them to
   * String.
   */
  private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("Y-M-d");

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  /**
   * Global instance of the maximum page size, which will be the default page size for samples with
   * pagination.
   */
  private static final Integer MAXIMUM_PAGE_SIZE = 50;

  /**
   * Authorizes the application to access the user's protected data.
   *
   * @throws IOException if the {@code JSON_FILE} can not be read.
   * @return An instantiated GoogleCredentials instance.
   */
  private static GoogleCredentials authorize() throws IOException {
    GoogleCredentials credentials;

    try (FileInputStream serviceAccountStream = new FileInputStream((JSON_FILE))) {
      Set<String> scopes = new HashSet<>(AuthorizedBuyersMarketplaceScopes.all());

      credentials = ServiceAccountCredentials.fromStream(serviceAccountStream).createScoped(scopes);
    }

    return credentials;
  }

  /** Helper method to produce an appropriate indent for the given indentLevel. */
  private static String getIndent(int indentLevel) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < indentLevel; i++) {
      builder.append('\t');
    }

    return builder.toString();
  }

  /** Helper method to produce a prefix for a printed field. */
  private static String getPrefix(int indentLevel) {
    StringBuilder builder = new StringBuilder();

    builder.append(getIndent(indentLevel));

    // Root-level messages will be denoted with an asterisk. Otherwise, fields will be denoted with
    // a hyphen.
    char bullet = (indentLevel == 0) ? '*' : '-';

    return builder.append(bullet).toString();
  }

  /** Helper method to print a {@code String} field. */
  private static void printField(String fieldDesc, String field, int indentLevel) {
    if (field == null) {
      return;
    }

    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %s%n", prefix, fieldDesc, field);
  }

  /** Helper method to print a {@code String} field. */
  private static void printField(
      String fieldDesc, String field, String defaultValue, int indentLevel) {
    field = (field != null) ? field : defaultValue;
    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %s%n", prefix, fieldDesc, field);
  }

  /** Helper method to print an {@code Integer} field. */
  private static void printField(String fieldDesc, Integer field, int indentLevel) {
    if (field == null) {
      return;
    }

    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %d%n", prefix, fieldDesc, field);
  }

  /** Helper method to print an {@code Integer} field. */
  private static void printField(
      String fieldDesc, Integer field, Integer defaultValue, int indentLevel) {
    field = (field != null) ? field : defaultValue;
    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %d%n", prefix, fieldDesc, field);
  }

  /** Helper method to print a {@code Long} field. */
  private static void printField(String fieldDesc, Long field, int indentLevel) {
    if (field == null) {
      return;
    }

    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %d%n", prefix, fieldDesc, field);
  }

  /** Helper method to print a {@code Long} field. */
  private static void printField(String fieldDesc, Long field, Long defaultValue, int indentLevel) {
    field = (field != null) ? field : defaultValue;
    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %d%n", prefix, fieldDesc, field);
  }

  /** Helper method to print a {@code Double} field. */
  private static void printField(String fieldDesc, Double field, int indentLevel) {
    if (field == null) {
      return;
    }

    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %f%n", prefix, fieldDesc, field);
  }

  /** Helper method to print a {@code Double} field. */
  private static void printField(
      String fieldDesc, Double field, Double defaultValue, int indentLevel) {
    field = (field != null) ? field : defaultValue;
    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %f%n", prefix, fieldDesc, field);
  }

  /** Helper method to print a {@code Boolean} field. */
  private static void printField(String fieldDesc, Boolean field, int indentLevel) {
    if (field == null) {
      return;
    }

    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %b%n", prefix, fieldDesc, field);
  }

  /** Helper method to print a {@code Boolean} field. */
  private static void printField(
      String fieldDesc, Boolean field, Boolean defaultValue, int indentLevel) {
    field = (field != null) ? field : defaultValue;
    String prefix = getPrefix(indentLevel);
    System.out.printf("%s%s: %b%n", prefix, fieldDesc, field);
  }

  private static void printField(String fieldDesc, DealPausingInfo field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Pausing consented", field.getPausingConsented(), indentLevel);
    printField("Pause role", field.getPauseRole(), indentLevel);
    printField("Pause reason", field.getPauseReason(), indentLevel);
  }

  private static void printField(String fieldDesc, RtbMetrics field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Bid requests over last 7 days", field.getBidRequests7Days(), 0L, indentLevel);
    printField("Bids over last 7 days", field.getBids7Days(), 0L, indentLevel);
    printField("Ad impressions over last 7 days", field.getAdImpressions7Days(), 0L, indentLevel);
    printField("Bid rate over last 7 days", field.getBidRate7Days(), 0.0, indentLevel);
    printField(
        "Filtered bid rate over last 7 days", field.getFilteredBidRate7Days(), 0.0, indentLevel);
    printField(
        "Must bid rate for current month", field.getMustBidRateCurrentMonth(), 0.0, indentLevel);
  }

  private static void printField(String fieldDesc, Money field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Currency code", field.getCurrencyCode(), indentLevel);
    printField("Units", field.getUnits(), 0L, indentLevel);
    printField("Nanos", field.getNanos(), 0, indentLevel);
  }

  private static void printField(String fieldDesc, TimeZone field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("ID", field.getId(), indentLevel);
    printField("Version", field.getVersion(), indentLevel);
  }

  private static void printField(String fieldDesc, CriteriaTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printLongList("Targeted Criteria IDs", field.getTargetedCriteriaIds(), indentLevel);
    printLongList("Excluded Criteria IDs", field.getTargetedCriteriaIds(), indentLevel);
  }

  private static void printField(String fieldDesc, AdSize field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Width", field.getWidth(), indentLevel);
    printField("Height", field.getHeight(), indentLevel);
  }

  private static void printField(String fieldDesc, InventorySizeTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printAdSizeList("Targeted inventory sizes", field.getTargetedInventorySizes(), indentLevel);
    printAdSizeList("Excluded inventory sizes", field.getExcludedInventorySizes(), indentLevel);
  }

  private static void printField(
      String fieldDesc, OperatingSystemTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Operating system criteria", field.getOperatingSystemCriteria(), indentLevel);
    printField(
        "Operating system version criteria",
        field.getOperatingSystemVersionCriteria(),
        indentLevel);
  }

  private static void printField(String fieldDesc, TechnologyTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Device category targeting", field.getDeviceCategoryTargeting(), indentLevel);
    printField("Device capability targeting", field.getDeviceCapabilityTargeting(), indentLevel);
    printField("Operating system targeting", field.getOperatingSystemTargeting(), indentLevel);
  }

  private static void printField(String fieldDesc, UriTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printStringList("Targeted URIs", field.getTargetedUris(), indentLevel);
    printStringList("Excluded URIs", field.getExcludedUris(), indentLevel);
  }

  private static void printField(
      String fieldDesc, FirstPartyMobileApplicationTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printStringList("Targeted App IDs", field.getTargetedAppIds(), indentLevel);
    printStringList("Excluded App IDs", field.getExcludedAppIds(), indentLevel);
  }

  private static void printField(
      String fieldDesc, MobileApplicationTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField(
        "First-party mobile application targeting", field.getFirstPartyTargeting(), indentLevel);
  }

  private static void printField(String fieldDesc, PlacementTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("URI targeting", field.getUriTargeting(), indentLevel);
    printField("Mobile application targeting", field.getMobileApplicationTargeting(), indentLevel);
  }

  private static void printField(String fieldDesc, TimeOfDay field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Hours", field.getHours(), indentLevel);
    printField("Minutes", field.getMinutes(), indentLevel);
    printField("Seconds", field.getSeconds(), indentLevel);
    printField("Nanos", field.getNanos(), indentLevel);
  }

  private static void printField(String fieldDesc, DayPart field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Day of week", field.getDayOfWeek(), indentLevel);
    printField("Start time", field.getStartTime(), indentLevel);
    printField("End time", field.getEndTime(), indentLevel);
  }

  private static void printField(String fieldDesc, DayPartTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printDayPartList("Day parts", field.getDayParts(), indentLevel);
    printField("Time zone type", field.getTimeZoneType(), indentLevel);
  }

  private static void printField(String fieldDesc, VideoTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printStringList("Targeted position types", field.getTargetedPositionTypes(), indentLevel);
    printStringList("Excluded position types", field.getExcludedPositionTypes(), indentLevel);
  }

  private static void printField(String fieldDesc, MarketplaceTargeting field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Geo targeting", field.getGeoTargeting(), indentLevel);
    printField("Inventory size targeting", field.getInventorySizeTargeting(), indentLevel);
    printField("Technology targeting", field.getTechnologyTargeting(), indentLevel);
    printField("Placement targeting", field.getPlacementTargeting(), indentLevel);
    printField("Video targeting", field.getVideoTargeting(), indentLevel);
    printField("User list targeting", field.getUserListTargeting(), indentLevel);
    printField("Day part targeting", field.getDaypartTargeting(), indentLevel);
  }

  private static void printField(String fieldDesc, CreativeRequirements field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Creative preapproval policy", field.getCreativePreApprovalPolicy(), indentLevel);
    printField(
        "Creative safeframe compatibility", field.getCreativeSafeFrameCompatibility(), indentLevel);
    printField("Programmatic creative source", field.getProgrammaticCreativeSource(), indentLevel);
  }

  private static void printField(String fieldDesc, FrequencyCap field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Max impressions", field.getMaxImpressions(), indentLevel);
    printField("Time units count", field.getTimeUnitsCount(), indentLevel);
    printField("Time unit type", field.getTimeUnitType(), indentLevel);
  }

  private static void printField(String fieldDesc, DeliveryControl field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Delivery rate type", field.getDeliveryRateType(), indentLevel);
    printFrequencyCapList("Frequency caps", field.getFrequencyCap(), indentLevel);
    printField("Road blocking type", field.getRoadblockingType(), indentLevel);
    printField("Companion delivery type", field.getCompanionDeliveryType(), indentLevel);
    printField("Creative rotation type", field.getCreativeRotationType(), indentLevel);
  }

  private static void printField(String fieldDesc, Price field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Type", field.getType(), indentLevel);
    printField("Amount", field.getAmount(), indentLevel);
  }

  private static void printField(
      String fieldDesc, ProgrammaticGuaranteedTerms field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Guaranteed looks", field.getGuaranteedLooks(), indentLevel);
    printField("Fixed price", field.getFixedPrice(), indentLevel);
    printField("Minimum daily looks", field.getMinimumDailyLooks(), indentLevel);
    printField("Reservation type", field.getReservationType(), indentLevel);
    printField("Impression cap", field.getImpressionCap(), indentLevel);
    printField("Percent share of voice", field.getPercentShareOfVoice(), indentLevel);
  }

  private static void printField(String fieldDesc, PreferredDealTerms field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Fixed price", field.getFixedPrice(), indentLevel);
  }

  private static void printField(String fieldDesc, PrivateAuctionTerms field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Floor price", field.getFloorPrice(), indentLevel);
    printField("Open auction allowed", field.getOpenAuctionAllowed(), indentLevel);
  }

  private static void printField(String fieldDesc, PrivateData field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Reference ID", field.getReferenceId(), indentLevel);
  }

  private static void printField(String fieldDesc, Deal field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Deal name", field.getName(), indentLevel);
    printField("Create time", field.getCreateTime(), indentLevel);
    printField("Update time", field.getUpdateTime(), indentLevel);
    printField("Proposal revision", field.getProposalRevision(), indentLevel);
    printField("Display name", field.getDisplayName(), indentLevel);
    printField("Billed buyer", field.getBilledBuyer(), indentLevel);
    printField("Proposal revision", field.getProposalRevision(), indentLevel);
    printField("Publisher profile", field.getPublisherProfile(), indentLevel);
    printField("Deal type", field.getDealType(), indentLevel);
    printField("Estimated gross spend", field.getEstimatedGrossSpend(), indentLevel);
    printField("Seller time zone", field.getSellerTimeZone(), indentLevel);
    printField("Description", field.getDescription(), indentLevel);
    printField("Flight start time", field.getFlightStartTime(), indentLevel);
    printField("Flight end time", field.getFlightEndTime(), indentLevel);
    printField("Marketplace targeting", field.getTargeting(), indentLevel);
    printField("Creative requirements", field.getCreativeRequirements(), indentLevel);
    printField("Delivery control", field.getDeliveryControl(), indentLevel);
    printField("Buyer", field.getBuyer(), indentLevel);
    printField("Client", field.getClient(), indentLevel);
    printField(
        "Programmatic guaranteed terms", field.getProgrammaticGuaranteedTerms(), indentLevel);
    printField("Preferred deal terms", field.getPreferredDealTerms(), indentLevel);
    printField("Private auction terms", field.getPrivateAuctionTerms(), indentLevel);
  }

  private static void printField(String fieldDesc, Contact field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Email", field.getEmail(), indentLevel);
    printField("Display name", field.getDisplayName(), indentLevel);
  }

  private static void printField(String fieldDesc, Note field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Create time", field.getCreateTime(), indentLevel);
    printField("Creator role", field.getCreatorRole(), indentLevel);
    printField("Note value", field.getNote(), indentLevel);
  }

  private static void printField(
      String fieldDesc, PublisherProfileMobileApplication field, int indentLevel) {
    if (field == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    printField("Name", field.getName(), indentLevel);
    printField("App Store", field.getAppStore(), indentLevel);
    printField("External App ID", field.getExternalAppId(), indentLevel);
  }

  /** Helper method to print a {@code List} of {@code String} values. */
  private static void printStringList(String fieldDesc, List<String> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (String item : values) {
      System.out.printf("%s%s%n", getPrefix(indentLevel), item);
    }
  }

  /** Helper method to print a {@code List} of {@code Integer} values. */
  private static void printIntegerList(String fieldDesc, List<Integer> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (Integer item : values) {
      System.out.printf("%s%d%n", getPrefix(indentLevel), item);
    }
  }

  /** Helper method to print a {@code List} of {@code Long} values. */
  private static void printLongList(String fieldDesc, List<Long> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (Long item : values) {
      System.out.printf("%s%d%n", getPrefix(indentLevel), item);
    }
  }

  /** Helper method to print a {@code List} of {@code AdSize} values. */
  private static void printAdSizeList(String fieldDesc, List<AdSize> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (AdSize adSize : values) {
      printField("AdSize", adSize, indentLevel);
    }
  }

  /** Helper method to print a {@code List} of {@code Contact} values. */
  private static void printContactList(String fieldDesc, List<Contact> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (Contact contact : values) {
      printField("Contact", contact, indentLevel);
    }
  }

  /** Helper method to print a {@code List} of {@code DayPart} values. */
  private static void printDayPartList(String fieldDesc, List<DayPart> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (DayPart dayPart : values) {
      printField("Day part", dayPart, indentLevel);
    }
  }

  /** Helper method to print a {@code List} of {@code FrequencyCap} values. */
  private static void printFrequencyCapList(
      String fieldDesc, List<FrequencyCap> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (FrequencyCap frequencyCap : values) {
      printField("Frequency cap", frequencyCap, indentLevel);
    }
  }

  /** Helper method to print a {@code List} of {@code Note} values. */
  private static void printNoteList(String fieldDesc, List<Note> values, int indentLevel) {
    if (values == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (Note note : values) {
      printField("Note", note, indentLevel);
    }
  }

  /** Helper method to print a {@code List} of {@code PublisherProfileMobileApplication} values. */
  private static void printListOfPubProfileMobileApps(
      String fieldDesc, List<PublisherProfileMobileApplication> apps, int indentLevel) {
    if (apps == null) {
      return;
    }

    System.out.printf("%s%s:%n", getPrefix(indentLevel), fieldDesc);

    indentLevel++;
    for (PublisherProfileMobileApplication app : apps) {
      printField("Publisher profile mobile application", app, indentLevel);
    }
  }

  /**
   * Retrieve a {@code DateTimeFormatter} instance used to parse and serialize {@code LocalDate}.
   *
   * @return An initialized {@code DateTimeFormatter} instance.
   */
  public static DateTimeFormatter getDateTimeFormatterForLocalDate() {
    return dateFormatter;
  }

  /**
   * Retrieve the default maximum page size.
   *
   * @return An Integer representing the default maximum page size for samples with pagination.
   */
  public static Integer getMaximumPageSize() {
    return MAXIMUM_PAGE_SIZE;
  }

  /**
   * Performs all necessary setup steps for running requests against the Marketplace API.
   *
   * @return An initialized AuthorizedBuyersMarketplace service object.
   */
  public static AuthorizedBuyersMarketplace getMarketplaceClient()
      throws IOException, GeneralSecurityException {
    GoogleCredentials credentials = authorize();
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    return new AuthorizedBuyersMarketplace.Builder(httpTransport, JSON_FACTORY, requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  /** Prints a {@code AuctionPackage} instance in a human-readable format. */
  public static void printAuctionPackage(AuctionPackage auctionPackage) {
    int indentLevel = 0;

    printField("Auction package name", auctionPackage.getName(), indentLevel);

    indentLevel++;
    printField("Creator", auctionPackage.getCreator(), indentLevel);
    printField("Display name", auctionPackage.getDisplayName(), indentLevel);
    printField("Description", auctionPackage.getDescription(), indentLevel);
    printField("Create time", auctionPackage.getCreateTime(), indentLevel);
    printField("Update time", auctionPackage.getUpdateTime(), indentLevel);
    printStringList("Subscribed clients", auctionPackage.getSubscribedClients(), indentLevel);
  }

  /** Prints a {@code Client} instance in a human-readable format. */
  public static void printClient(Client client) {
    int indentLevel = 0;

    printField("Client name", client.getName(), indentLevel);

    indentLevel++;
    printField("Display name", client.getDisplayName(), indentLevel);
    printField("Partner client ID", client.getPartnerClientId(), indentLevel);
    printField("Role", client.getRole(), indentLevel);
    printField("State", client.getState(), indentLevel);
    printField("Seller visible", client.getSellerVisible(), indentLevel);
  }

  /** Prints a {@code ClientUser} instance in a human-readable format. */
  public static void printClientUser(ClientUser clientUser) {
    int indentLevel = 0;

    printField("Client user name", clientUser.getName(), indentLevel);

    indentLevel++;
    printField("State", clientUser.getState(), indentLevel);
    printField("Email", clientUser.getEmail(), indentLevel);
  }

  /** Prints a {@code Deal} instance in a human-readable format. */
  public static void printDeal(Deal deal) {
    int indentLevel = 0;

    printField("Deal name", deal.getName(), indentLevel);

    indentLevel++;
    printField("Create time", deal.getCreateTime(), indentLevel);
    printField("Update time", deal.getUpdateTime(), indentLevel);
    printField("Proposal revision", deal.getProposalRevision(), indentLevel);
    printField("Display name", deal.getDisplayName(), indentLevel);
    printField("Billed buyer", deal.getBilledBuyer(), indentLevel);
    printField("Proposal revision", deal.getProposalRevision(), indentLevel);
    printField("Publisher profile", deal.getPublisherProfile(), indentLevel);
    printField("Deal type", deal.getDealType(), indentLevel);
    printField("Estimated gross spend", deal.getEstimatedGrossSpend(), indentLevel);
    printField("Seller time zone", deal.getSellerTimeZone(), indentLevel);
    printField("Description", deal.getDescription(), indentLevel);
    printField("Flight start time", deal.getFlightStartTime(), indentLevel);
    printField("Flight end time", deal.getFlightEndTime(), indentLevel);
    printField("Marketplace targeting", deal.getTargeting(), indentLevel);
    printField("Creative requirements", deal.getCreativeRequirements(), indentLevel);
    printField("Delivery control", deal.getDeliveryControl(), indentLevel);
    printField("Buyer", deal.getBuyer(), indentLevel);
    printField("Client", deal.getClient(), indentLevel);
    printField("Programmatic guaranteed terms", deal.getProgrammaticGuaranteedTerms(), indentLevel);
    printField("Preferred deal terms", deal.getPreferredDealTerms(), indentLevel);
    printField("Private auction terms", deal.getPrivateAuctionTerms(), indentLevel);
  }

  /** Prints a {@code Proposal} instance in a human-readable format. */
  public static void printProposal(Proposal proposal) {
    int indentLevel = 0;

    printField("Proposal name", proposal.getName(), indentLevel);

    indentLevel++;
    printField("Display name", proposal.getDisplayName(), indentLevel);
    printField("Update time", proposal.getUpdateTime(), indentLevel);
    printField("Proposal revision", proposal.getProposalRevision(), indentLevel);
    printField("Deal type", proposal.getDealType(), indentLevel);
    printField("Is renegotiating", proposal.getIsRenegotiating(), indentLevel);
    printField("Originator role", proposal.getOriginatorRole(), indentLevel);
    printField("Publisher profile", proposal.getPublisherProfile(), indentLevel);
    printField("Buyer private data", proposal.getBuyerPrivateData(), indentLevel);
    printField("Billed buyer", proposal.getBilledBuyer(), indentLevel);
    printContactList("Seller contacts", proposal.getSellerContacts(), indentLevel);
    printContactList("Buyer contacts", proposal.getBuyerContacts(), indentLevel);
    printField(
        "Last updater or commenter role", proposal.getLastUpdaterOrCommentorRole(), indentLevel);
    printField("Terms and conditions", proposal.getTermsAndConditions(), indentLevel);
    printField("Pausing consented", proposal.getPausingConsented(), indentLevel);
    printNoteList("Notes", proposal.getNotes(), indentLevel);
    printField("Buyer", proposal.getBuyer(), indentLevel);
    printField("Client", proposal.getClient(), indentLevel);
  }

  /** Prints a {@code PublisherProfile} instance in a human-readable format. */
  public static void printPublisherProfile(PublisherProfile publisherProfile) {
    int indentLevel = 0;

    printField("Publisher profile name", publisherProfile.getName(), indentLevel);

    indentLevel++;
    printField("Display name", publisherProfile.getDisplayName(), indentLevel);
    printStringList("Domains", publisherProfile.getDomains(), indentLevel);
    printListOfPubProfileMobileApps("Mobile apps", publisherProfile.getMobileApps(), indentLevel);
    printField("Logo URL", publisherProfile.getLogoUrl(), indentLevel);
    printField("Direct deals contact", publisherProfile.getDirectDealsContact(), indentLevel);
    printField(
        "Programmatic deals contact", publisherProfile.getProgrammaticDealsContact(), indentLevel);
    printField("Media kit URL", publisherProfile.getMediaKitUrl(), indentLevel);
    printField("Sample page URL", publisherProfile.getSamplePageUrl(), indentLevel);
    printField("Overview", publisherProfile.getOverview(), indentLevel);
    printField("Pitch statement", publisherProfile.getPitchStatement(), indentLevel);
    printStringList("Top headlines", publisherProfile.getTopHeadlines(), indentLevel);
    printField("Audience description", publisherProfile.getAudienceDescription(), indentLevel);
    printField("Is parent", publisherProfile.getIsParent(), indentLevel);
    printField("Publisher code", publisherProfile.getPublisherCode(), indentLevel);
  }

  /** Prints a {@code FinalizedDeal} instance in a human-readable format. */
  public static void printFinalizedDeal(FinalizedDeal finalizedDeal) {
    int indentLevel = 0;

    printField("Finalized deal name", finalizedDeal.getName(), indentLevel);

    indentLevel++;
    printField("Deal serving status", finalizedDeal.getDealServingStatus(), indentLevel);
    printField("Deal pausing info", finalizedDeal.getDealPausingInfo(), indentLevel);
    printField("RTB metrics", finalizedDeal.getRtbMetrics(), indentLevel);
    printField("Ready to serve", finalizedDeal.getReadyToServe(), indentLevel);
    printField("Deal", finalizedDeal.getDeal(), indentLevel);
  }
}

/* Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

using Google.Apis.AuthorizedBuyersMarketplace.v1;
using Google.Apis.AuthorizedBuyersMarketplace.v1.Data;
using Google.Apis.Auth.OAuth2;
using Google.Apis.Http;
using Google.Apis.Json;
using Mono.Options;

using System;
using System.Collections.Generic;

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples
{
    /// <summary>
    /// Utilities used by the Authorized Buyers Marketplace API samples.
    /// </summary>
    public class Utilities
    {
        /// <summary>
        /// Full path to a JSON key file to be used in the Service Account OAuth 2.0 flow. See
        /// README.md for more details.
        /// </summary>
        private static String ServiceKeyFilePath = "PATH TO JSON KEYFILE";

        /// <summary>
        /// The default maximum page size to be used for API calls featuring pagination.
        /// </summary>
        public static int MAX_PAGE_SIZE = 50;

        /// <summary>
        /// Print a human-readable representation of a single auction package.
        /// </summary>
        public static void PrintAuctionPackage(AuctionPackage auctionPackage)
        {
            Console.WriteLine("* Auction Package name: {0}", auctionPackage.Name);

            string creator = auctionPackage.Creator;
            if (creator != null)
            {
                Console.WriteLine("\t- Creator: {0}", creator);
            }

            string displayName = auctionPackage.DisplayName;
            if (displayName != null)
            {
                Console.WriteLine("\t- Display name: {0}", displayName);
            }

            string description = auctionPackage.Description;
            if (description != null)
            {
                Console.WriteLine("\t- Description: {0}", description);
            }

            object createTime = auctionPackage.CreateTime;
            if (createTime != null)
            {
                Console.WriteLine("\t- Create time: {0}", createTime.ToString());
            }

            object updateTime = auctionPackage.UpdateTime;
            if (updateTime != null)
            {
                Console.WriteLine("\t- Update time: {0}", updateTime.ToString());
            }

            IList<string> subscribedClients = auctionPackage.SubscribedClients;
            if (subscribedClients != null)
            {
                Console.WriteLine("\t- Subscribed clients:\n\t\t" +
                    String.Join("\n\t\t", subscribedClients));
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single client.
        /// </summary>
        public static void PrintClient(Client client)
        {
            Console.WriteLine("* Client name: {0}", client.Name);

            string displayName = client.DisplayName;
            if (displayName != null)
            {
                Console.WriteLine("\t- Display Name: {0}", displayName);
            }

            string partnerClientId = client.PartnerClientId;
            if (partnerClientId != null)
            {
                Console.WriteLine("\t- Partner Client ID: {0}", partnerClientId);
            }

            string role = client.Role;
            if (role != null)
            {
                Console.WriteLine("\t- Role: {0}", role);
            }

            string state = client.State;
            if (state != null)
            {
                Console.WriteLine("\t- State: {0}", state);
            }

            bool? sellerVisible = client.SellerVisible;
            if (sellerVisible != null)
            {
                Console.WriteLine("\t- Seller Visible: {0}", sellerVisible);
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single client user.
        /// </summary>
        public static void PrintClientUser(ClientUser user)
        {
            Console.WriteLine("* Client user name: {0}", user.Name);

            string state = user.State;
            if (state != null)
            {
                Console.WriteLine("\t- State: {0}", state);
            }

            string email = user.Email;
            if (email != null)
            {
                Console.WriteLine("\t- Email: {0}", email);
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single deal.
        /// </summary>
        public static void PrintDeal(Deal deal)
        {
            Console.WriteLine("* Deal name: {0}", deal.Name);

            object createTime = deal.CreateTime;
            if (createTime != null)
            {
                Console.WriteLine("\t- Create time: {0}", createTime.ToString());
            }

            object updateTime = deal.UpdateTime;
            if (updateTime != null)
            {
                Console.WriteLine("\t- Update time: {0}", updateTime.ToString());
            }

            long? proposalRevision = deal.ProposalRevision;
            if (proposalRevision != null)
            {
                Console.WriteLine("\t- Proposal revision: {0}", proposalRevision);
            }

            string displayName = deal.DisplayName;
            if (displayName != null)
            {
                Console.WriteLine("\t- Display name: {0}", displayName);
            }

            string billedBuyer = deal.BilledBuyer;
            if (billedBuyer != null)
            {
                Console.WriteLine("\t- Billed buyer: {0}", billedBuyer);
            }

            string publisherProfile = deal.PublisherProfile;
            if (publisherProfile != null)
            {
                Console.WriteLine("\t- Publisher profile: {0}", publisherProfile);
            }

            string dealType = deal.DealType;
            if (dealType != null)
            {
                Console.WriteLine("\t- Deal type: {0}", dealType);
            }

            Money estimatedGrossSpend = deal.EstimatedGrossSpend;
            if (estimatedGrossSpend != null)
            {
                Console.WriteLine("\t- Estimated gross spend:");
                Console.WriteLine("\t\t- Currency code: {0}",
                    estimatedGrossSpend.CurrencyCode);
                Console.WriteLine("\t\t- Units: {0}",
                    estimatedGrossSpend.Units ?? 0L);
                Console.WriteLine("\t\t- Nanos: {0}",
                    estimatedGrossSpend.Nanos ?? 0L);
            }

            Google.Apis.AuthorizedBuyersMarketplace.v1.Data.TimeZone sellerTimeZone =
                deal.SellerTimeZone;
            if (sellerTimeZone != null)
            {
                Console.WriteLine("\t- Seller time zone:");
                Console.WriteLine("\t\t- ID: {0}", sellerTimeZone.Id);

                string version = sellerTimeZone.Version;
                if (version != null)
                {
                    Console.WriteLine("\t\t- Version: {0}",
                        sellerTimeZone.Version);
                }
            }

            string description = deal.Description;
            if (description != null)
            {
                Console.WriteLine("\t- Description: {0}", description);
            }

            object flightStartTime = deal.FlightStartTime;
            if (flightStartTime != null)
            {
                Console.WriteLine("\t- Flight start time: {0}", flightStartTime.ToString());
            }

            object flightEndTime = deal.FlightEndTime;
            if (flightEndTime != null)
            {
                Console.WriteLine("\t- Flight end time: {0}", flightEndTime.ToString());
            }

            MarketplaceTargeting targeting = deal.Targeting;
            if (targeting != null)
            {
                Console.WriteLine("\t- Marketplace targeting:");
                CriteriaTargeting geoTargeting = targeting.GeoTargeting;
                if (geoTargeting != null)
                {
                    Console.WriteLine("\t\t- Geo targeting:");

                    IList<long?> targetedCriteriaIds = geoTargeting.TargetedCriteriaIds;
                    if (targetedCriteriaIds != null)
                    {
                        Console.WriteLine("\t\t\t" +
                            String.Join("\n\t\t\t", targetedCriteriaIds));
                    }

                    IList<long?> excludedCriteriaIds = geoTargeting.ExcludedCriteriaIds;
                    if (excludedCriteriaIds != null)
                    {
                        Console.WriteLine("\t\t\t" +
                            String.Join("\n\t\t\t", excludedCriteriaIds));
                    }
                }

                InventorySizeTargeting inventorySizeTargeting =
                    targeting.InventorySizeTargeting;
                if (inventorySizeTargeting != null)
                {
                    Console.WriteLine("\t\t- Inventory size targeting:");

                    IList<AdSize> targetedInventorySizes =
                        inventorySizeTargeting.TargetedInventorySizes;
                    if (targetedInventorySizes != null)
                    {
                        Console.WriteLine("\t\t\t- Targeted inventory sizes:");
                        foreach (AdSize adSize in targetedInventorySizes)
                        {
                            Console.WriteLine("\t\t\t\t- Targeted inventory size:");
                            Console.WriteLine("\t\t\t\t\t- Width: {0}", adSize.Width);
                            Console.WriteLine("\t\t\t\t\t- Height: {0}", adSize.Height);
                        }
                    }

                    IList<AdSize> excludedInventorySizes =
                        inventorySizeTargeting.ExcludedInventorySizes;
                    if (excludedInventorySizes != null)
                    {
                        Console.WriteLine("\t\t\t- Excluded inventory sizes:");
                        foreach (AdSize adSize in excludedInventorySizes)
                        {
                            Console.WriteLine("\t\t\t\t- Excluded inventory size:");
                            Console.WriteLine("\t\t\t\t\t- Width: {0}", adSize.Width);
                            Console.WriteLine("\t\t\t\t\t- Height: {0}", adSize.Height);
                        }
                    }
                }

                TechnologyTargeting technologyTargeting = targeting.TechnologyTargeting;
                if (technologyTargeting != null)
                {
                    Console.WriteLine("\t\t- Technology targeting:");

                    CriteriaTargeting deviceCategoryTargeting =
                        technologyTargeting.DeviceCategoryTargeting;
                    if (deviceCategoryTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Device category targeting:");

                        IList<long?> targetedCriteriaIds =
                            deviceCategoryTargeting.TargetedCriteriaIds;
                        if (targetedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\n\t\t\t\t", targetedCriteriaIds));
                        }

                        IList<long?> excludedCriteriaIds =
                            deviceCategoryTargeting.ExcludedCriteriaIds;
                        if (excludedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\n\t\t\t\t", excludedCriteriaIds));
                        }
                    }

                    CriteriaTargeting deviceCapabilityTargeting =
                        technologyTargeting.DeviceCapabilityTargeting;
                    if (deviceCapabilityTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Device capability targeting:");

                        IList<long?> targetedCriteriaIds =
                            deviceCapabilityTargeting.TargetedCriteriaIds;
                        if (targetedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\n\t\t\t\t", targetedCriteriaIds));
                        }

                        IList<long?> excludedCriteriaIds =
                            deviceCapabilityTargeting.ExcludedCriteriaIds;
                        if (excludedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\n\t\t\t\t", excludedCriteriaIds));
                        }
                    }

                    OperatingSystemTargeting operatingSystemTargeting =
                        technologyTargeting.OperatingSystemTargeting;
                    if (operatingSystemTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Operating system targeting:");

                        CriteriaTargeting operatingSystemCriteria =
                            operatingSystemTargeting.OperatingSystemCriteria;
                        if (operatingSystemCriteria != null)
                        {
                            Console.WriteLine("\t\t\t\t- Operating system criteria:");

                            IList<long?> targetedCriteriaIds =
                                operatingSystemCriteria.TargetedCriteriaIds;
                            if (targetedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t", targetedCriteriaIds));
                            }

                            IList<long?> excludedCriteriaIds =
                                operatingSystemCriteria.ExcludedCriteriaIds;
                            if (excludedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t" +
                                    String.Join("\n\t\t\t\t", excludedCriteriaIds));
                            }
                        }

                        CriteriaTargeting operatingSystemVersionCriteria =
                            operatingSystemTargeting.OperatingSystemVersionCriteria;
                        if (operatingSystemVersionCriteria != null)
                        {
                            Console.WriteLine("\t\t\t\t- Operating system version criteria:");

                            IList<long?> targetedCriteriaIds =
                                operatingSystemVersionCriteria.TargetedCriteriaIds;
                            if (targetedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t", targetedCriteriaIds));
                            }

                            IList<long?> excludedCriteriaIds =
                                operatingSystemVersionCriteria.ExcludedCriteriaIds;
                            if (excludedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t", excludedCriteriaIds));
                            }
                        }
                    }
                }

                PlacementTargeting placementTargeting = targeting.PlacementTargeting;
                if (placementTargeting != null)
                {
                    Console.WriteLine("\t\t- Placement targeting:");

                    UriTargeting uriTargeting = placementTargeting.UriTargeting;
                    if (uriTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- URI targeting:");

                        IList<string> targetedUris = uriTargeting.TargetedUris;
                        if (targetedUris != null)
                        {
                            Console.WriteLine("\t\t\t\t- Targeted URIs:");
                            Console.WriteLine("\t\t\t\t\t" +
                                String.Join("\n\t\t\t\t\t", targetedUris));
                        }

                        IList<string> excludedUris = uriTargeting.ExcludedUris;
                        if (excludedUris != null)
                        {
                            Console.WriteLine("\t\t\t\t- Excluded URIs:");
                            Console.WriteLine("\t\t\t\t\t" +
                                String.Join("\n\t\t\t\t\t", excludedUris));
                        }
                    }

                    MobileApplicationTargeting mobileApplicationTargeting =
                        placementTargeting.MobileApplicationTargeting;
                    if (mobileApplicationTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Mobile application targeting:");

                        FirstPartyMobileApplicationTargeting fpMobileApplicationTargeting =
                            mobileApplicationTargeting.FirstPartyTargeting;
                        if (fpMobileApplicationTargeting != null)
                        {
                            Console.WriteLine("\t\t\t\t- First party mobile application " +
                                "targeting:");

                            IList<string> targetedAppIds =
                                fpMobileApplicationTargeting.TargetedAppIds;
                            if (targetedAppIds != null)
                            {
                                Console.WriteLine("\t\t\t\t\t- Targeted App IDs:");
                                Console.WriteLine("\t\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t\t", targetedAppIds));
                            }

                            IList<string> excludedAppIds =
                                fpMobileApplicationTargeting.ExcludedAppIds;
                            if (excludedAppIds != null)
                            {
                                Console.WriteLine("\t\t\t\t\t- Excluded App IDs:");
                                Console.WriteLine("\t\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t\t", excludedAppIds));
                            }
                        }
                    }
                }

                VideoTargeting videoTargeting = targeting.VideoTargeting;
                if (videoTargeting != null)
                {
                    Console.WriteLine("\t\t- Video targeting:");

                    IList<string> targetedPositionTypes = videoTargeting.TargetedPositionTypes;
                    if (targetedPositionTypes != null)
                    {
                        Console.WriteLine("\t\t\t- Targeted position types:");
                        Console.WriteLine("\t\t\t\t" +
                            String.Join("\n\t\t\t\t", targetedPositionTypes));
                    }

                    IList<string> excludedPositionTypes = videoTargeting.ExcludedPositionTypes;
                    if (excludedPositionTypes != null)
                    {
                        Console.WriteLine("\t\t\t- Excluded position types:");
                        Console.WriteLine("\t\t\t\t" +
                            String.Join("\n\t\t\t\t", excludedPositionTypes));
                    }
                }

                CriteriaTargeting userListTargeting = targeting.UserListTargeting;
                if (userListTargeting != null)
                {
                    Console.WriteLine("\t\t- User list targeting:");

                    IList<long?> targetedCriteriaIds = userListTargeting.TargetedCriteriaIds;
                    if (targetedCriteriaIds != null)
                    {
                        Console.WriteLine("\t\t\t" +
                            String.Join("\t\t\t", targetedCriteriaIds));
                    }

                    IList<long?> excludedCriteriaIds = userListTargeting.ExcludedCriteriaIds;
                    if (excludedCriteriaIds != null)
                    {
                        Console.WriteLine("\t\t\t" +
                            String.Join("\t\t\t", excludedCriteriaIds));
                    }
                }

                DayPartTargeting dayPartTargeting = targeting.DaypartTargeting;
                if (dayPartTargeting != null)
                {
                    Console.WriteLine("\t\t\t- Day part targeting:");

                    foreach (DayPart dayPart in dayPartTargeting.DayParts)
                    {
                        TimeOfDay startTime = dayPart.StartTime;
                        TimeOfDay endTime = dayPart.EndTime;
                        Console.WriteLine("\t\t\t- Day part:");
                        Console.WriteLine("\t\t\t\t- Day of week: {0}", dayPart.DayOfWeek);
                        Console.WriteLine("\t\t\t\t- Start time:");
                        Console.WriteLine("\t\t\t\t\t- Hours: {0}", startTime.Hours);
                        Console.WriteLine("\t\t\t\t\t- Minutes: {0}", startTime.Minutes);
                        Console.WriteLine("\t\t\t\t\t- Seconds: {0}", startTime.Seconds);
                        Console.WriteLine("\t\t\t\t\t- Nanos: {0}", startTime.Nanos);
                        Console.WriteLine("\t\t\t\t- End time:");
                        Console.WriteLine("\t\t\t\t\t- Hours: {0}", endTime.Hours);
                        Console.WriteLine("\t\t\t\t\t- Minutes: {0}", endTime.Minutes);
                        Console.WriteLine("\t\t\t\t\t- Seconds: {0}", endTime.Seconds);
                        Console.WriteLine("\t\t\t\t\t- Nanos: {0}", endTime.Nanos);
                    }

                    Console.WriteLine("\t\t\t- Time zone type: {0}",
                        dayPartTargeting.TimeZoneType);
                }
            }

            CreativeRequirements creativeRequirements = deal.CreativeRequirements;
            if (creativeRequirements != null)
            {
                Console.WriteLine("\t\t- Creative requirements:");
                string creativePreApprovalPolicy = creativeRequirements.CreativePreApprovalPolicy;
                if (creativePreApprovalPolicy != null)
                {
                    Console.WriteLine("\t\t\t- Creative preapproval policy: {0}",
                        creativePreApprovalPolicy);
                }

                string creativeSafeFrameCompatibility =
                    creativeRequirements.CreativeSafeFrameCompatibility;
                if (creativeSafeFrameCompatibility != null)
                {
                    Console.WriteLine("\t\t\t- Creative safeFrame compatibility: {0}",
                        creativeSafeFrameCompatibility);
                }

                string programmaticCreativeSource =
                    creativeRequirements.ProgrammaticCreativeSource;
                if (programmaticCreativeSource != null)
                {
                    Console.WriteLine("\t\t\t- Programmatic creative source: {0}",
                        programmaticCreativeSource);
                }
            }

            DeliveryControl deliveryControl = deal.DeliveryControl;
            if (deliveryControl != null)
            {
                string deliveryRateType = deliveryControl.DeliveryRateType;
                if (deliveryRateType != null)
                {
                    Console.WriteLine("\t\t\t- Delivery rate type: {0}", deliveryRateType);
                }

                IList<FrequencyCap> frequencyCaps = deliveryControl.FrequencyCap;
                if (frequencyCaps != null)
                {
                    Console.WriteLine("\t\t\t- Frequency caps:");

                    foreach (FrequencyCap frequencyCap in frequencyCaps)
                    {
                        Console.WriteLine("\t\t\t\t- Frequency cap:");
                        Console.WriteLine("\t\t\t\t\t- maxImpressions: {0}",
                                frequencyCap.MaxImpressions);
                        Console.WriteLine("\t\t\t\t\t- timeUnitsCount: {0}",
                                frequencyCap.TimeUnitsCount);
                        Console.WriteLine("\t\t\t\t\t- timeUnitType: {0}",
                                frequencyCap.TimeUnitType);
                    }
                }

                string roadBlockingType = deliveryControl.RoadblockingType;
                if (roadBlockingType != null)
                {
                    Console.WriteLine("\t\t\t- Road blocking type: {0}", roadBlockingType);
                }

                string companionDeliveryType = deliveryControl.CompanionDeliveryType;
                if (companionDeliveryType != null)
                {
                    Console.WriteLine("\t\t\t- Companion delivery type: {0}",
                            companionDeliveryType);
                }

                string creativeRotationType = deliveryControl.CreativeRotationType;
                if (creativeRotationType != null)
                {
                    Console.WriteLine("\t\t\t- Creative rotation type: {0}", creativeRotationType);
                }
            }

            string buyer = deal.Buyer;
            if (buyer != null)
            {
                Console.WriteLine("\t- Buyer: {0}", buyer);
            }

            string client = deal.Client;
            if (client != null)
            {
                Console.WriteLine("\t- Client: {0}", client);
            }

            ProgrammaticGuaranteedTerms programmaticGuaranteedTerms =
                deal.ProgrammaticGuaranteedTerms;
            if (programmaticGuaranteedTerms != null)
            {
                Console.WriteLine("\t- Programmatic guaranteed terms:");

                long? guaranteedLooks = programmaticGuaranteedTerms.GuaranteedLooks;
                if (guaranteedLooks != null)
                {
                    Console.WriteLine("\t\t- Guaranteed looks: {0}", guaranteedLooks);
                }

                Price fixedPrice = programmaticGuaranteedTerms.FixedPrice;
                if (fixedPrice != null)
                {
                    Console.WriteLine("\t\t- Fixed price:");
                    Console.WriteLine("\t\t\t- Type: {0}", fixedPrice.Type);

                    Money amount = fixedPrice.Amount;
                    if (amount != null)
                    {
                        Console.WriteLine("\t\t\t- Amount:");
                        Console.WriteLine("\t\t\t\t- currencyCode: {0}", amount.CurrencyCode);
                        Console.WriteLine("\t\t\t\t- Units: {0}", amount.Units ?? 0L);
                        Console.WriteLine("\t\t\t\t- Nanos: {0}", amount.Nanos ?? 0L);
                    }
                }

                long? minimumDailyLooks = programmaticGuaranteedTerms.MinimumDailyLooks;
                if (minimumDailyLooks != null)
                {
                    Console.WriteLine("\t\t- Minimum daily looks: {0}", minimumDailyLooks);
                }

                string reservationType = programmaticGuaranteedTerms.ReservationType;
                if (reservationType != null)
                {
                    Console.WriteLine("\t\t- Reservation type: {0}", reservationType);
                }

                long? impressionCap = programmaticGuaranteedTerms.ImpressionCap;
                if (impressionCap != null)
                {
                    Console.WriteLine("\t\t- Impression cap: {0}", impressionCap);
                }

                long? percentShareOfVoice = programmaticGuaranteedTerms.PercentShareOfVoice;
                if (percentShareOfVoice != null)
                {
                    Console.WriteLine("\t\t- Percent share of voice: {0}",
                        percentShareOfVoice);
                }
            }

            PreferredDealTerms preferredDealTerms = deal.PreferredDealTerms;
            if (preferredDealTerms != null)
            {
                Console.WriteLine("\t- Preferred deal terms:");

                Price fixedPrice = preferredDealTerms.FixedPrice;
                if (fixedPrice != null)
                {
                    Console.WriteLine("\t\t- Fixed price:");
                    Console.WriteLine("\t\t\t- Type: {0}", fixedPrice.Type);

                    Money amount = fixedPrice.Amount;
                    if (amount != null)
                    {
                        Console.WriteLine("\t\t\t- Amount:");
                        Console.WriteLine("\t\t\t\t- currencyCode: {0}", amount.CurrencyCode);
                        Console.WriteLine("\t\t\t\t- Units: {0}", amount.Units ?? 0L);
                        Console.WriteLine("\t\t\t\t- Nanos: {0}", amount.Nanos ?? 0L);
                    }
                }
            }

            PrivateAuctionTerms privateAuctionTerms = deal.PrivateAuctionTerms;
            if (privateAuctionTerms != null)
            {
                Console.WriteLine("\t- Private auction terms:");

                Price floorPrice = privateAuctionTerms.FloorPrice;
                if (floorPrice != null)
                {
                    Console.WriteLine("\t\t- Floor price:");
                    Console.WriteLine("\t\t\t- Type: {0}", floorPrice.Type);

                    Money amount = floorPrice.Amount;
                    if (amount != null)
                    {
                        Console.WriteLine("\t\t\t- Amount:");
                        Console.WriteLine("\t\t\t\t- currencyCode: {0}", amount.CurrencyCode);
                        Console.WriteLine("\t\t\t\t- Units: {0}", amount.Units ?? 0L);
                        Console.WriteLine("\t\t\t\t- Nanos: {0}", amount.Nanos ?? 0L);
                    }
                }

                bool? openAuctionAllowed = privateAuctionTerms.OpenAuctionAllowed;
                if (openAuctionAllowed != null)
                {
                    Console.WriteLine("\t\t\t- Open auction allowed: {0}",
                        openAuctionAllowed);
                }
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single finalized deal.
        /// </summary>
        public static void PrintFinalizedDeal(FinalizedDeal finalizedDeal)
        {
            Console.WriteLine("* Finalized deal name: {0}", finalizedDeal.Name);

            string dealServingStatus = finalizedDeal.DealServingStatus;
            if (dealServingStatus != null)
            {
                Console.WriteLine("\t- Deal serving status: {0}", dealServingStatus);
            }

            DealPausingInfo dealPausingInfo = finalizedDeal.DealPausingInfo;
            if (dealPausingInfo != null)
            {
                Console.WriteLine("\t- Deal pausing info:");

                bool? pausingConsented = dealPausingInfo.PausingConsented;
                if (pausingConsented != null)
                {
                    Console.WriteLine("\t\t- Pausing consented: {0}",
                        pausingConsented);
                }

                string pauseRole = dealPausingInfo.PauseRole;
                if (pauseRole != null)
                {
                    Console.WriteLine("\t\t- Pause role: {0}",
                        pauseRole);
                }

                string pauseReason = dealPausingInfo.PauseReason;
                if (pauseReason != null)
                {
                    Console.WriteLine("\t\t- Pause reason: {0}",
                        pauseReason);
                }
            }

            RtbMetrics rtbMetrics = finalizedDeal.RtbMetrics;
            if (rtbMetrics != null)
            {
                Console.WriteLine("\t- RTB metrics:");
                Console.WriteLine("\t\t- Bid requests over last 7 days: {0}",
                    rtbMetrics.BidRequests7Days ?? 0L);
                Console.WriteLine("\t\t- Bids over last 7 days: {0}",
                    rtbMetrics.Bids7Days ?? 0L);
                Console.WriteLine("\t\t- Ad impressions over last 7 days: {0}",
                    rtbMetrics.AdImpressions7Days ?? 0L);
                Console.WriteLine("\t\t- Bid rate over last 7 days: {0}",
                    rtbMetrics.BidRate7Days ?? 0L);
                Console.WriteLine("\t\t- Filtered bid rate over last 7 days: {0}",
                    rtbMetrics.FilteredBidRate7Days ?? 0L);
                Console.WriteLine("\t\t- Must bid rate for current month: {0}",
                    rtbMetrics.MustBidRateCurrentMonth ?? 0L);
            }

            bool? readyToServe = finalizedDeal.ReadyToServe;
            if (readyToServe != null)
            {
                Console.WriteLine("\t- Ready to serve: {0}", readyToServe);
            }

            Deal deal = finalizedDeal.Deal;
            if (deal != null)
            {
                Console.WriteLine("\t- Deal name: {0}", deal.Name);

                object createTime = deal.CreateTime;
                if (createTime != null)
                {
                    Console.WriteLine("\t\t- Create time: {0}", createTime.ToString());
                }

                object updateTime = deal.UpdateTime;
                if (updateTime != null)
                {
                    Console.WriteLine("\t\t- Update time: {0}", updateTime.ToString());
                }

                long? proposalRevision = deal.ProposalRevision;
                if (proposalRevision != null)
                {
                    Console.WriteLine("\t\t- Proposal revision: {0}", proposalRevision);
                }

                string displayName = deal.DisplayName;
                if (displayName != null)
                {
                    Console.WriteLine("\t\t- Display name: {0}", displayName);
                }

                string billedBuyer = deal.BilledBuyer;
                if (billedBuyer != null)
                {
                    Console.WriteLine("\t\t- Billed buyer: {0}", billedBuyer);
                }

                string publisherProfile = deal.PublisherProfile;
                if (publisherProfile != null)
                {
                    Console.WriteLine("\t\t- Publisher profile: {0}", publisherProfile);
                }

                string dealType = deal.DealType;
                if (dealType != null)
                {
                    Console.WriteLine("\t\t- Deal type: {0}", dealType);
                }

                Money estimatedGrossSpend = deal.EstimatedGrossSpend;
                if (estimatedGrossSpend != null)
                {
                    Console.WriteLine("\t\t- Estimated gross spend:");
                    Console.WriteLine("\t\t\t- Currency code: {0}",
                        estimatedGrossSpend.CurrencyCode);
                    Console.WriteLine("\t\t\t- Units: {0}",
                        estimatedGrossSpend.Units ?? 0L);
                    Console.WriteLine("\t\t\t- Nanos: {0}",
                        estimatedGrossSpend.Nanos ?? 0L);
                }

                Google.Apis.AuthorizedBuyersMarketplace.v1.Data.TimeZone sellerTimeZone =
                    deal.SellerTimeZone;
                if (sellerTimeZone != null)
                {
                    Console.WriteLine("\t\t- Seller time zone:");
                    Console.WriteLine("\t\t\t- ID: {0}",
                        sellerTimeZone.Id);

                    string version = sellerTimeZone.Version;
                    if (version != null)
                    {
                        Console.WriteLine("\t\t\t- Version: {0}",
                            sellerTimeZone.Version);
                    }
                }

                string description = deal.Description;
                if (description != null)
                {
                    Console.WriteLine("\t\t- Description: {0}", description);
                }

                object flightStartTime = deal.FlightStartTime;
                if (flightStartTime != null)
                {
                    Console.WriteLine("\t\t- Flight start time: {0}", flightStartTime.ToString());
                }

                object flightEndTime = deal.FlightEndTime;
                if (flightEndTime != null)
                {
                    Console.WriteLine("\t\t- Flight end time: {0}", flightEndTime.ToString());
                }

                MarketplaceTargeting targeting = deal.Targeting;
                if (targeting != null)
                {
                    Console.WriteLine("\t\t- Marketplace targeting:");
                    CriteriaTargeting geoTargeting = targeting.GeoTargeting;
                    if (geoTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Geo targeting:");

                        IList<long?> targetedCriteriaIds = geoTargeting.TargetedCriteriaIds;
                        if (targetedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\n\t\t\t\t", targetedCriteriaIds));
                        }

                        IList<long?> excludedCriteriaIds = geoTargeting.ExcludedCriteriaIds;
                        if (excludedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\n\t\t\t\t", excludedCriteriaIds));
                        }
                    }

                    InventorySizeTargeting inventorySizeTargeting =
                        targeting.InventorySizeTargeting;
                    if (inventorySizeTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Inventory size targeting:");

                        IList<AdSize> targetedInventorySizes =
                            inventorySizeTargeting.TargetedInventorySizes;
                        if (targetedInventorySizes != null)
                        {
                            Console.WriteLine("\t\t\t\t- Targeted inventory sizes:");
                            foreach (AdSize adSize in targetedInventorySizes)
                            {
                                Console.WriteLine("\t\t\t\t\t- Targeted inventory size:");
                                Console.WriteLine("\t\t\t\t\t\t- Width: {0}", adSize.Width);
                                Console.WriteLine("\t\t\t\t\t\t- Height: {0}", adSize.Height);
                            }
                        }

                        IList<AdSize> excludedInventorySizes =
                            inventorySizeTargeting.ExcludedInventorySizes;
                        if (excludedInventorySizes != null)
                        {
                            Console.WriteLine("\t\t\t\t- Excluded inventory sizes:");
                            foreach (AdSize adSize in excludedInventorySizes)
                            {
                                Console.WriteLine("\t\t\t\t\t- Excluded inventory size:");
                                Console.WriteLine("\t\t\t\t\t\t- Width: {0}", adSize.Width);
                                Console.WriteLine("\t\t\t\t\t\t- Height: {0}", adSize.Height);
                            }
                        }
                    }

                    TechnologyTargeting technologyTargeting = targeting.TechnologyTargeting;
                    if (technologyTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Technology targeting:");

                        CriteriaTargeting deviceCategoryTargeting =
                            technologyTargeting.DeviceCategoryTargeting;
                        if (deviceCategoryTargeting != null)
                        {
                            Console.WriteLine("\t\t\t\t- Device category targeting:");

                            IList<long?> targetedCriteriaIds =
                                deviceCategoryTargeting.TargetedCriteriaIds;
                            if (targetedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t", targetedCriteriaIds));
                            }

                            IList<long?> excludedCriteriaIds =
                                deviceCategoryTargeting.ExcludedCriteriaIds;
                            if (excludedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t" +
                                    String.Join("\n\t\t\t\t", excludedCriteriaIds));
                            }
                        }

                        CriteriaTargeting deviceCapabilityTargeting =
                            technologyTargeting.DeviceCapabilityTargeting;
                        if (deviceCapabilityTargeting != null)
                        {
                            Console.WriteLine("\t\t\t\t- Device capability targeting:");

                            IList<long?> targetedCriteriaIds =
                                deviceCapabilityTargeting.TargetedCriteriaIds;
                            if (targetedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t", targetedCriteriaIds));
                            }

                            IList<long?> excludedCriteriaIds =
                                deviceCapabilityTargeting.ExcludedCriteriaIds;
                            if (excludedCriteriaIds != null)
                            {
                                Console.WriteLine("\t\t\t\t" +
                                    String.Join("\n\t\t\t\t", excludedCriteriaIds));
                            }
                        }

                        OperatingSystemTargeting operatingSystemTargeting =
                            technologyTargeting.OperatingSystemTargeting;
                        if (operatingSystemTargeting != null)
                        {
                            Console.WriteLine("\t\t\t\t- Operating system targeting:");

                            CriteriaTargeting operatingSystemCriteria =
                                operatingSystemTargeting.OperatingSystemCriteria;
                            if (operatingSystemCriteria != null)
                            {
                                Console.WriteLine("\t\t\t\t\t- Operating system criteria:");

                                IList<long?> targetedCriteriaIds =
                                    operatingSystemCriteria.TargetedCriteriaIds;
                                if (targetedCriteriaIds != null)
                                {
                                    Console.WriteLine("\t\t\t\t\t\t" +
                                        String.Join("\n\t\t\t\t\t\t", targetedCriteriaIds));
                                }

                                IList<long?> excludedCriteriaIds =
                                    operatingSystemCriteria.ExcludedCriteriaIds;
                                if (excludedCriteriaIds != null)
                                {
                                    Console.WriteLine("\t\t\t\t\t" +
                                        String.Join("\n\t\t\t\t\t", excludedCriteriaIds));
                                }
                            }

                            CriteriaTargeting operatingSystemVersionCriteria =
                                operatingSystemTargeting.OperatingSystemVersionCriteria;
                            if (operatingSystemVersionCriteria != null)
                            {
                                Console.WriteLine("\t\t\t\t\t- Operating system version " +
                                    "criteria:");

                                IList<long?> targetedCriteriaIds =
                                    operatingSystemVersionCriteria.TargetedCriteriaIds;
                                if (targetedCriteriaIds != null)
                                {
                                    Console.WriteLine("\t\t\t\t\t\t" +
                                        String.Join("\n\t\t\t\t\t\t", targetedCriteriaIds));
                                }

                                IList<long?> excludedCriteriaIds =
                                    operatingSystemVersionCriteria.ExcludedCriteriaIds;
                                if (excludedCriteriaIds != null)
                                {
                                    Console.WriteLine("\t\t\t\t\t" +
                                        String.Join("\n\t\t\t\t\t", excludedCriteriaIds));
                                }
                            }
                        }
                    }

                    PlacementTargeting placementTargeting = targeting.PlacementTargeting;
                    if (placementTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Placement targeting:");

                        UriTargeting uriTargeting = placementTargeting.UriTargeting;
                        if (uriTargeting != null)
                        {
                            Console.WriteLine("\t\t\t\t- URI targeting:");

                            IList<string> targetedUris = uriTargeting.TargetedUris;
                            if (targetedUris != null)
                            {
                                Console.WriteLine("\t\t\t\t\t- Targeted URIs:");
                                Console.WriteLine("\t\t\t\t\t\t" +
                                     String.Join("\n\t\t\t\t\t\t", targetedUris));
                            }

                            IList<string> excludedUris = uriTargeting.ExcludedUris;
                            if (excludedUris != null)
                            {
                                Console.WriteLine("\t\t\t\t\t- Excluded URIs:");
                                Console.WriteLine("\t\t\t\t\t\t" +
                                    String.Join("\n\t\t\t\t\t\t", excludedUris));
                            }
                        }

                        MobileApplicationTargeting mobileApplicationTargeting =
                            placementTargeting.MobileApplicationTargeting;
                        if (mobileApplicationTargeting != null)
                        {
                            Console.WriteLine("\t\t\t\t- Mobile application targeting:");

                            FirstPartyMobileApplicationTargeting fpMobileApplicationTargeting =
                                mobileApplicationTargeting.FirstPartyTargeting;
                            if (fpMobileApplicationTargeting != null)
                            {
                                Console.WriteLine("\t\t\t\t\t- First party mobile application " +
                                    "targeting:");


                                IList<string> targetedAppIds =
                                    fpMobileApplicationTargeting.TargetedAppIds;
                                if (targetedAppIds != null)
                                {
                                    Console.WriteLine("\t\t\t\t\t\t- Targeted App IDs:");
                                    Console.WriteLine("\t\t\t\t\t\t\t" +
                                        String.Join("\n\t\t\t\t\t\t\t", targetedAppIds));
                                }

                                IList<string> excludedAppIds =
                                    fpMobileApplicationTargeting.ExcludedAppIds;
                                if (excludedAppIds != null)
                                {
                                    Console.WriteLine("\t\t\t\t\t\t- Excluded App IDs:");
                                    Console.WriteLine("\t\t\t\t\t\t\t" +
                                        String.Join("\n\t\t\t\t\t\t\t", excludedAppIds));
                                }
                            }
                        }
                    }

                    VideoTargeting videoTargeting = targeting.VideoTargeting;
                    if (videoTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Video targeting:");

                        IList<string> targetedPositionTypes = videoTargeting.TargetedPositionTypes;
                        if (targetedPositionTypes != null)
                        {
                            Console.WriteLine("\t\t\t\t- Targeted position types:");
                            Console.WriteLine("\t\t\t\t\t" +
                                String.Join("\n\t\t\t\t\t", targetedPositionTypes));
                        }

                        IList<string> excludedPositionTypes = videoTargeting.ExcludedPositionTypes;
                        if (excludedPositionTypes != null)
                        {
                            Console.WriteLine("\t\t\t\t- Excluded position types:");
                            Console.WriteLine("\t\t\t\t\t" +
                                String.Join("\n\t\t\t\t\t", excludedPositionTypes));
                        }
                    }

                    CriteriaTargeting userListTargeting = targeting.UserListTargeting;
                    if (userListTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- User list targeting:");

                        IList<long?> targetedCriteriaIds = userListTargeting.TargetedCriteriaIds;
                        if (targetedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\t\t\t\t", targetedCriteriaIds));
                        }

                        IList<long?> excludedCriteriaIds = userListTargeting.ExcludedCriteriaIds;
                        if (excludedCriteriaIds != null)
                        {
                            Console.WriteLine("\t\t\t\t" +
                                String.Join("\t\t\t\t", excludedCriteriaIds));
                        }
                    }

                    DayPartTargeting dayPartTargeting = targeting.DaypartTargeting;
                    if (dayPartTargeting != null)
                    {
                        Console.WriteLine("\t\t\t- Day part targeting:");

                        foreach (DayPart dayPart in dayPartTargeting.DayParts)
                        {
                            TimeOfDay startTime = dayPart.StartTime;
                            TimeOfDay endTime = dayPart.EndTime;
                            Console.WriteLine("\t\t\t\t- Day part:");
                            Console.WriteLine("\t\t\t\t\t- Day of week: {0}", dayPart.DayOfWeek);
                            Console.WriteLine("\t\t\t\t\t- Start time:");
                            Console.WriteLine("\t\t\t\t\t\t- Hours: {0}", startTime.Hours);
                            Console.WriteLine("\t\t\t\t\t\t- Minutes: {0}", startTime.Minutes);
                            Console.WriteLine("\t\t\t\t\t\t- Seconds: {0}", startTime.Seconds);
                            Console.WriteLine("\t\t\t\t\t\t- Nanos: {0}", startTime.Nanos);
                            Console.WriteLine("\t\t\t\t\t- End time:");
                            Console.WriteLine("\t\t\t\t\t\t- Hours: {0}", endTime.Hours);
                            Console.WriteLine("\t\t\t\t\t\t- Minutes: {0}", endTime.Minutes);
                            Console.WriteLine("\t\t\t\t\t\t- Seconds: {0}", endTime.Seconds);
                            Console.WriteLine("\t\t\t\t\t\t- Nanos: {0}", endTime.Nanos);
                        }

                        Console.WriteLine("\t\t\t\t- Time zone type: {0}",
                            dayPartTargeting.TimeZoneType);
                    }
                }

                CreativeRequirements creativeRequirements = deal.CreativeRequirements;
                if (creativeRequirements != null)
                {
                    Console.WriteLine("\t\t\t- Creative requirements:");
                    string creativePreApprovalPolicy =
                        creativeRequirements.CreativePreApprovalPolicy;
                    if (creativePreApprovalPolicy != null)
                    {
                        Console.WriteLine("\t\t\t\t- Creative preapproval policy: {0}",
                            creativePreApprovalPolicy);
                    }

                    string creativeSafeFrameCompatibility =
                        creativeRequirements.CreativeSafeFrameCompatibility;
                    if (creativeSafeFrameCompatibility != null)
                    {
                        Console.WriteLine("\t\t\t\t- Creative safeFrame compatibility: {0}",
                            creativeSafeFrameCompatibility);
                    }

                    string programmaticCreativeSource =
                        creativeRequirements.ProgrammaticCreativeSource;
                    if (programmaticCreativeSource != null)
                    {
                        Console.WriteLine("\t\t\t\t- Programmatic creative source: {0}",
                            programmaticCreativeSource);
                    }
                }

                DeliveryControl deliveryControl = deal.DeliveryControl;
                if (deliveryControl != null)
                {
                    string deliveryRateType = deliveryControl.DeliveryRateType;
                    if (deliveryRateType != null)
                    {
                        Console.WriteLine("\t\t\t\t- Delivery rate type: {0}", deliveryRateType);
                    }

                    IList<FrequencyCap> frequencyCaps = deliveryControl.FrequencyCap;
                    if (frequencyCaps != null)
                    {
                        Console.WriteLine("\t\t\t\t- Frequency caps:");

                        foreach (FrequencyCap frequencyCap in frequencyCaps)
                        {
                            Console.WriteLine("\t\t\t\t\t- Frequency cap:");
                            Console.WriteLine("\t\t\t\t\t\t- maxImpressions: {0}",
                                frequencyCap.MaxImpressions);
                            Console.WriteLine("\t\t\t\t\t\t- timeUnitsCount: {0}",
                                frequencyCap.TimeUnitsCount);
                            Console.WriteLine("\t\t\t\t\t\t- timeUnitType: {0}",
                                frequencyCap.TimeUnitType);
                        }
                    }

                    string roadBlockingType = deliveryControl.RoadblockingType;
                    if (roadBlockingType != null)
                    {
                        Console.WriteLine("\t\t\t\t- Road blocking type: {0}", roadBlockingType);
                    }

                    string companionDeliveryType = deliveryControl.CompanionDeliveryType;
                    if (companionDeliveryType != null)
                    {
                        Console.WriteLine("\t\t\t\t- Companion delivery type: {0}",
                            companionDeliveryType);
                    }

                    string creativeRotationType = deliveryControl.CreativeRotationType;
                    if (creativeRotationType != null)
                    {
                        Console.WriteLine("\t\t\t\t- Creative rotation type: {0}",
                            creativeRotationType);
                    }
                }

                string buyer = deal.Buyer;
                if (buyer != null)
                {
                    Console.WriteLine("\t\t\t- Buyer: {0}", buyer);
                }

                string client = deal.Client;
                if (client != null)
                {
                    Console.WriteLine("\t\t\t- Client: {0}", client);
                }

                ProgrammaticGuaranteedTerms programmaticGuaranteedTerms =
                    deal.ProgrammaticGuaranteedTerms;
                if (programmaticGuaranteedTerms != null)
                {
                    Console.WriteLine("\t\t\t- Programmatic guaranteed terms:");

                    long? guaranteedLooks = programmaticGuaranteedTerms.GuaranteedLooks;
                    if (guaranteedLooks != null)
                    {
                        Console.WriteLine("\t\t\t\t- Guaranteed looks: {0}", guaranteedLooks);
                    }

                    Price fixedPrice = programmaticGuaranteedTerms.FixedPrice;
                    if (fixedPrice != null)
                    {
                        Console.WriteLine("\t\t\t\t- Fixed price:");
                        Console.WriteLine("\t\t\t\t\t- Type: {0}", fixedPrice.Type);

                        Money amount = fixedPrice.Amount;
                        if (amount != null)
                        {
                            Console.WriteLine("\t\t\t\t\t- Amount:");
                            Console.WriteLine("\t\t\t\t\t\t- currencyCode: {0}",
                                amount.CurrencyCode);
                            Console.WriteLine("\t\t\t\t\t\t- Units: {0}", amount.Units ?? 0L);
                            Console.WriteLine("\t\t\t\t\t\t- Nanos: {0}", amount.Nanos ?? 0L);
                        }
                    }

                    long? minimumDailyLooks = programmaticGuaranteedTerms.MinimumDailyLooks;
                    if (minimumDailyLooks != null)
                    {
                        Console.WriteLine("\t\t\t\t- Minimum daily looks: {0}", minimumDailyLooks);
                    }

                    string reservationType = programmaticGuaranteedTerms.ReservationType;
                    if (reservationType != null)
                    {
                        Console.WriteLine("\t\t\t\t- Reservation type: {0}", reservationType);
                    }

                    long? impressionCap = programmaticGuaranteedTerms.ImpressionCap;
                    if (impressionCap != null)
                    {
                        Console.WriteLine("\t\t\t\t- Impression cap: {0}", impressionCap);
                    }

                    long? percentShareOfVoice = programmaticGuaranteedTerms.PercentShareOfVoice;
                    if (percentShareOfVoice != null)
                    {
                        Console.WriteLine("\t\t\t\t- Percent share of voice: {0}",
                            percentShareOfVoice);
                    }
                }

                PreferredDealTerms preferredDealTerms = deal.PreferredDealTerms;
                if (preferredDealTerms != null)
                {
                    Console.WriteLine("\t\t\t- Preferred deal terms:");

                    Price fixedPrice = preferredDealTerms.FixedPrice;
                    if (fixedPrice != null)
                    {
                        Console.WriteLine("\t\t\t\t- Fixed price:");
                        Console.WriteLine("\t\t\t\t\t- Type: {0}", fixedPrice.Type);

                        Money amount = fixedPrice.Amount;
                        if (amount != null)
                        {
                            Console.WriteLine("\t\t\t\t\t- Amount:");
                            Console.WriteLine("\t\t\t\t\t\t- currencyCode: {0}",
                                amount.CurrencyCode);
                            Console.WriteLine("\t\t\t\t\t\t- Units: {0}", amount.Units ?? 0L);
                            Console.WriteLine("\t\t\t\t\t\t- Nanos: {0}", amount.Nanos ?? 0L);
                        }
                    }
                }

                PrivateAuctionTerms privateAuctionTerms = deal.PrivateAuctionTerms;
                if (privateAuctionTerms != null)
                {
                    Console.WriteLine("\t\t\t- Private auction terms:");

                    Price floorPrice = privateAuctionTerms.FloorPrice;
                    if (floorPrice != null)
                    {
                        Console.WriteLine("\t\t\t\t- Floor price:");
                        Console.WriteLine("\t\t\t\t\t- Type: {0}", floorPrice.Type);

                        Money amount = floorPrice.Amount;
                        if (amount != null)
                        {
                            Console.WriteLine("\t\t\t\t\t- Amount:");
                            Console.WriteLine("\t\t\t\t\t\t- currencyCode: {0}",
                                amount.CurrencyCode);
                            Console.WriteLine("\t\t\t\t\t\t- Units: {0}", amount.Units ?? 0L);
                            Console.WriteLine("\t\t\t\t\t\t- Nanos: {0}", amount.Nanos ?? 0L);
                        }
                    }

                    bool? openAuctionAllowed = privateAuctionTerms.OpenAuctionAllowed;
                    if (openAuctionAllowed != null)
                    {
                        Console.WriteLine("\t\t\t\t- Open auction allowed: {0}",
                            openAuctionAllowed);
                    }
                }
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single proposal.
        /// </summary>
        public static void PrintProposal(Proposal proposal)
        {
            Console.WriteLine("* Proposal name: {0}", proposal.Name);

            string displayName = proposal.DisplayName;
            if (displayName != null)
            {
                Console.WriteLine("\t- Display name: {0}", displayName);
            }

            object updateTime = proposal.UpdateTime;
            if (updateTime != null)
            {
                Console.WriteLine("\t- Update time: {0}", updateTime.ToString());
            }

            long? proposalRevision = proposal.ProposalRevision;
            if (proposalRevision != null)
            {
                Console.WriteLine("\t- Proposal revision: {0}", proposalRevision.ToString());
            }

            string dealType = proposal.DealType;
            if (dealType != null)
            {
                Console.WriteLine("\t- Deal type: {0}", dealType);
            }

            bool? isRenegotiating = proposal.IsRenegotiating;
            if (isRenegotiating != null)
            {
                Console.WriteLine("\t- Is renegotiating: {0}", isRenegotiating);
            }

            string originatorRole = proposal.OriginatorRole;
            if (originatorRole != null)
            {
                Console.WriteLine("\t- Originator role: {0}", originatorRole);
            }

            string publisherProfile = proposal.PublisherProfile;
            if (publisherProfile != null)
            {
                Console.WriteLine("\t- Publisher profile: {0}", publisherProfile);
            }

            PrivateData buyerPrivateData = proposal.BuyerPrivateData;
            if (buyerPrivateData != null)
            {
                Console.WriteLine("\t- Buyer private data:");
                Console.WriteLine("\t\t- Reference ID: {0}", buyerPrivateData.ReferenceId);
            }

            string billedBuyer = proposal.BilledBuyer;
            if (billedBuyer != null)
            {
                Console.WriteLine("\t- Billed buyer: {0}", billedBuyer);
            }

            IList<Contact> sellerContacts = proposal.SellerContacts;
            if (sellerContacts != null)
            {
                Console.WriteLine("\t- Seller contacts:");

                foreach (Contact sellerContact in sellerContacts)
                {
                    Console.WriteLine("\t\t- Seller contact:");
                    Console.WriteLine("\t\t\t- Email: {0}", sellerContact.Email);

                    string sellerDisplayName = sellerContact.DisplayName;
                    if (sellerDisplayName != null)
                    {
                        Console.WriteLine("\t\t\t- Display name: {0}", sellerDisplayName);
                    }
                }
            }

            IList<Contact> buyerContacts = proposal.BuyerContacts;
            if (buyerContacts != null)
            {
                Console.WriteLine("\t- Buyer contacts:");

                foreach (Contact buyerContact in buyerContacts)
                {
                    Console.WriteLine("\t\t- Buyer contact:");
                    Console.WriteLine("\t\t\t- Email: {0}", buyerContact.Email);

                    string buyerDisplayName = buyerContact.DisplayName;
                    if (buyerDisplayName != null)
                    {
                        Console.WriteLine("\t\t\t- Display name: {0}", buyerDisplayName);
                    }
                }
            }

            string lastUpdaterOrCommentorRole = proposal.LastUpdaterOrCommentorRole;
            if (lastUpdaterOrCommentorRole != null)
            {
                Console.WriteLine("\t- Last updater or commenter role: {0}",
                    lastUpdaterOrCommentorRole);
            }

            string termsAndConditions = proposal.TermsAndConditions;
            if (termsAndConditions != null)
            {
                Console.WriteLine("\t- Terms and conditions: {0}", termsAndConditions);
            }

            bool? pausingConsented = proposal.PausingConsented;
            if (pausingConsented != null)
            {
                Console.WriteLine("\t- Pausing consented: {0}", pausingConsented);
            }

            IList<Note> notes = proposal.Notes;
            if (notes != null)
            {
                Console.WriteLine("\t- Notes:");

                foreach (Note note in notes)
                {
                    Console.WriteLine("\t\t- Note:");
                    Console.WriteLine("\t\t\t- Create time: {0}", note.CreateTime);
                    Console.WriteLine("\t\t\t- Display name: {0}", note.CreatorRole);
                    Console.WriteLine("\t\t\t- Display name: {0}", note.NoteValue);

                }
            }

            string buyer = proposal.Buyer;
            if (buyer != null)
            {
                Console.WriteLine("\t- Buyer: {0}", buyer);
            }

            string client = proposal.Client;
            if (client != null)
            {
                Console.WriteLine("\t- Client: {0}", client);
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single publisher profile.
        /// </summary>
        public static void PrintPublisherProfile(PublisherProfile publisherProfile)
        {
            Console.WriteLine("* Publisher profile name: {0}", publisherProfile.Name);

            string displayName = publisherProfile.DisplayName;
            if (displayName != null)
            {
                Console.WriteLine("\t- Display name: {0}", displayName);
            }

            IList<string> domains = publisherProfile.Domains;
            if (domains != null)
            {
                Console.WriteLine("\t- Domains:\n\t\t" + String.Join("\n\t\t", domains));
            }

            IList<PublisherProfileMobileApplication> mobileApps = publisherProfile.MobileApps;
            if (mobileApps != null)
            {
                Console.WriteLine("\t- Mobile apps:");

                foreach (var mobileApp in mobileApps) {
                    Console.WriteLine("\t\t- Name: {0}", mobileApp.Name);
                    Console.WriteLine("\t\t\t- App Store: {0}", mobileApp.AppStore);
                    Console.WriteLine("\t\t\t- External App ID: {0}", mobileApp.ExternalAppId);
                }
            }

            string logoUrl = publisherProfile.LogoUrl;
            if (logoUrl != null)
            {
                Console.WriteLine("\t- Logo URL: {0}", logoUrl);
            }

            string directDealsContact = publisherProfile.DirectDealsContact;
            if (directDealsContact != null)
            {
                Console.WriteLine("\t- Direct Deals Contact: {0}", directDealsContact);
            }

            string programmaticDealsContact = publisherProfile.ProgrammaticDealsContact;
            if (programmaticDealsContact != null)
            {
                Console.WriteLine("\t- Programmatic Deals Contact: {0}", programmaticDealsContact);
            }

            string mediaKitUrl = publisherProfile.MediaKitUrl;
            if (mediaKitUrl != null)
            {
                Console.WriteLine("\t- Media Kit URL: {0}", mediaKitUrl);
            }

            string samplePageUrl = publisherProfile.SamplePageUrl;
            if (samplePageUrl != null)
            {
                Console.WriteLine("\t- Sample Page URL: {0}", samplePageUrl);
            }

            string overview = publisherProfile.Overview;
            if (overview != null)
            {
                Console.WriteLine("\t- Overview:\n{0}\n", overview);
            }

            string pitchStatement = publisherProfile.PitchStatement;
            if (pitchStatement != null)
            {
                Console.WriteLine("\t- Pitch Statement:\n{0}\n", pitchStatement);
            }

            IList<string> topHeadlines = publisherProfile.TopHeadlines;
            if (topHeadlines != null)
            {
                Console.WriteLine("\t- Top Headlines:\n\t\t" +
                    String.Join("\n\t\t", topHeadlines));
            }

            string audienceDescription = publisherProfile.AudienceDescription;
            if (audienceDescription != null)
            {
                Console.WriteLine("\t- Audience Description:\n{0}\n", audienceDescription);
            }

            bool? isParent = publisherProfile.IsParent;
            if (isParent != null)
            {
                Console.WriteLine("\t- Is Parent: {0}", isParent);
            }

            string publisherCode = publisherProfile.PublisherCode;
            if (publisherCode != null)
            {
                Console.WriteLine("\t- Publisher Code: {0}", publisherCode);
            }
        }

        /// <summary>
        /// Create a new service for the Authorized Buyers Real-time Bidding API.
        /// </summary>
        /// <returns>A new API Service</returns>
        public static AuthorizedBuyersMarketplaceService GetAuthorizedBuyersMarketplaceService()
        {
            return new AuthorizedBuyersMarketplaceService(
                new Google.Apis.Services.BaseClientService.Initializer
                {
                    HttpClientInitializer = ServiceAccount(),
                    ApplicationName = "Marketplace API C# Sample",
                }
            );
        }

        /// <summary>
        /// Uses a JSON KeyFile to authenticate a service account and return credentials for
        /// accessing the API.
        /// </summary>
        /// <returns>Authentication object for API Requests</returns>
        private static IConfigurableHttpClientInitializer ServiceAccount()
        {
            var credentialParameters = NewtonsoftJsonSerializer.Instance
                .Deserialize<JsonCredentialParameters>(System.IO.File.ReadAllText(
                    ServiceKeyFilePath));

            return new ServiceAccountCredential(
                new ServiceAccountCredential.Initializer(credentialParameters.ClientEmail)
                {
                    Scopes = new[] {
                        AuthorizedBuyersMarketplaceService.Scope.AuthorizedBuyersMarketplace
                    }
                }.FromPrivateKey(credentialParameters.PrivateKey));
        }

        /// <summary>
        /// Ensures that required options have been set, and that unknown options have not been
        /// specified. Otherwise exits the example with an error message.
        /// </summary>
        /// <returns>A new API Service</returns>
        public static void ValidateOptions(
            OptionSet options, Dictionary<string, object> parsedArgs, string[] requiredKeys,
            List<string> extras)
        {
            if (extras.Count > 0)
            {
                throw new ApplicationException("Unknown arguments specified:\n\t" +
                    String.Join("\t", extras));
            }

            foreach(string requiredKey in requiredKeys)
            {
                if (parsedArgs[requiredKey] == null)
                {
                    options.WriteOptionDescriptions(Console.Error);
                    throw new ApplicationException(String.Format(
                        @"Required argument ""{0}"" not specified.", requiredKey));
                }
            }
        }
    }
}

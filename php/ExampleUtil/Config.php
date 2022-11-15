<?php

/**
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil;

use DateTimeImmutable;
use Google_Client;
use Google_Service_AuthorizedBuyersMarketplace;
use Google_Service_AuthorizedBuyersMarketplace_Date;

/**
 * Defines configuration of the sample application.
 */
class Config
{
    /*
     * The current version of the Marketplace API.
     */
    private const API_VERSION = 'V1';

    /**
     * Date format used when converting between DateTime instances and strings.
     */
    private const DATE_TIME_FORMAT = 'Y-m-d\TH:i:s\Z';

    /**
     * Returns the current supported API version.
     */
    public static function getApiVersion()
    {
        return self::API_VERSION;
    }

    /**
     * Converts a DateTimeImmutable to a corresponding ISO 8601 datetime string.
     */
    public static function getDateStringFromDateTimeImmutable($dt)
    {
        return $dt->format(self::DATE_TIME_FORMAT);
    }

    /**
     * Steps through OAuth 2.0 and returns a Google_Client instance.
     */
    public static function getGoogleClient($keyFileLocation)
    {
        $client = new Google_Client();
        $client->setApplicationName('Authorized Buyers Marketplace API PHP Samples');

        if (isset($_SESSION['service_token'])) {
            $client->setAccessToken($_SESSION['service_token']);
        }

        $client->setAuthConfig($keyFileLocation);
        $client->addScope('https://www.googleapis.com/auth/authorized-buyers-marketplace');

        if ($client->isAccessTokenExpired()) {
            $client->refreshTokenWithAssertion();
        }

        return $client;
    }

    /**
     * Returns Google_Service_AuthorizedBuyersMarketplace, instantiated with the given Google_Client.
     */
    public static function getGoogleServiceAuthorizedBuyersMarketplace($client)
    {
        return new Google_Service_AuthorizedBuyersMarketplace($client);
    }

    /**
     * Builds an array containing the supported actions.
     */
    public static function getSupportedActions()
    {
        return [
           self::API_VERSION => [
               'Bidders_FinalizedDeals' => [
                   'ListFinalizedDeals'
               ],
               'Buyers_AuctionPackages' => [
                   'GetAuctionPackages',
                   'ListAuctionPackages',
                   'SubscribeToAuctionPackages',
                   'UnsubscribeFromAuctionPackages',
                   'SubscribeClientsToAuctionPackages',
                   'UnsubscribeClientsFromAuctionPackages'
               ],
               'Buyers_Clients' => [
                   'GetClients',
                   'ListClients',
                   'CreateClients',
                   'PatchClients',
                   'ActivateClients',
                   'DeactivateClients'
               ],
               'Buyers_Clients_Users' => [
                   'GetClientUsers',
                   'ListClientUsers',
                   'CreateClientUsers',
                   'ActivateClientUsers',
                   'DeactivateClientUsers',
                   'DeleteClientUsers'
               ],
               'Buyers_FinalizedDeals' => [
                   'GetFinalizedDeals',
                   'ListFinalizedDeals',
                   'AddCreativeToFinalizedDeals',
                   'SetReadyToServeFinalizedDeals',
                   'PauseFinalizedDeals',
                   'ResumeFinalizedDeals'
               ],
               'Buyers_Proposals' => [
                   'GetProposals',
                   'ListProposals',
                   'SendRfpForPreferredDealProposals',
                   'SendRfpForProgrammaticGuaranteedDealProposals',
                   'CancelNegotiationForProposals',
                   'AcceptProposals',
                   'PatchProposals',
                   'AddNoteToProposals'
               ],
               'Buyers_Proposals_Deals' => [
                   'GetDeals',
                   'ListDeals',
                   'PatchPreferredDeals',
                   'PatchProgrammaticGuaranteedDeals',
                   'BatchUpdateDeals'
               ],
               'Buyers_PublisherProfiles' => [
                   'GetPublisherProfiles',
                   'ListPublisherProfiles'
               ],
           ],
        ];
    }
}

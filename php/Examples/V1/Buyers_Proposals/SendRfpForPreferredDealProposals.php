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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Proposals;

use DateInterval;
use DateTimeImmutable;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\AdSize;
use Google\Service\AuthorizedBuyersMarketplace\Contact;
use Google\Service\AuthorizedBuyersMarketplace\CriteriaTargeting;
use Google\Service\AuthorizedBuyersMarketplace\InventorySizeTargeting;
use Google\Service\AuthorizedBuyersMarketplace\Money;
use Google\Service\AuthorizedBuyersMarketplace\PreferredDealTerms;
use Google\Service\AuthorizedBuyersMarketplace\Price;
use Google\Service\AuthorizedBuyersMarketplace\SendRfpRequest;

/**
 * This example illustrates how to send a request for proposal to a publisher for a preferred deal.
 *
 * The publisher will be sent an RFP that will initiate negotiation for a preferred deal. For the
 * buyer, this will create a corresponding proposal.
 *
 * You must refer to the publisher using their publisher profile. These can be found with the
 * buyers.publisherProfiles resource.
 */
class SendRfpForPreferredDealProposals extends BaseExample
{
    public function __construct($client)
    {
        $this->service = Config::getGoogleServiceAuthorizedBuyersMarketplace($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'account_id',
                'display' => 'Account ID',
                'description' =>
                    'The resource ID of the buyers resource on behalf of which the RFP is being ' .
                    'sent.',
                'required' => true
            ],
            [
                'name' => 'buyer_contacts_email',
                'display' => 'Buyer contacts email',
                'description' =>
                    'Email address for the buyer\'s contact, which will be visible to the ' .
                    'publisher.',
                'required' => true
            ],
            [
                'name' => 'buyer_contacts_display_name',
                'display' => 'Buyer contacts display name',
                'description' =>
                    'The display name of the buyer\'s contact, which will be visible to the ' .
                    'publisher.',
                'required' => true
            ],
            [
                'name' => 'publisher_profile_id',
                'display' => 'Publisher profile ID',
                'description' =>
                    'The resource ID of the publisher profiles resource representing the ' .
                    'publisher that the buyer wants to send the RFP.',
                'required' => true
            ],
            [
                'name' => 'display_name',
                'display' => 'Display name',
                'description' =>
                    'The display name of the proposal being created by the RFP.',
                'required' => false,
                'default' => 'Test PD Proposal #' . uniqid()
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $parentName = "buyers/$values[account_id]";
        $publisherProfileName = "$parentName/publisherProfiles/$values[publisher_profile_id]";

        $rfp = new SendRfpRequest();
        $rfp->displayName = $values['display_name'];
        $rfp->publisherProfile = $publisherProfileName;
        $rfp->note = 'Test preferred deal proposal created by PHP sample.';

        // Specify the start and end flight times in RFC3339 UTC "Zulu" format.
        $today = new DateTimeImmutable('today');
        $startDateTime = $today->add(new DateInterval('P7D'));
        $endDateTime = $startDateTime->add(new DateInterval('P1D'));
        $rfp->flightStartTime = Config::getDateStringFromDateTimeImmutable($startDateTime);
        $rfp->flightEndTime = Config::getDateStringFromDateTimeImmutable($endDateTime);

        $buyerContact = new Contact();
        $buyerContact->email = $values['buyer_contacts_email'];
        $buyerContact->displayName = $values['buyer_contacts_display_name'];
        $rfp->buyerContacts = [$buyerContact];

        $geoTargeting = new CriteriaTargeting();
        // Target New York, NY.
        $geoTargeting->targetedCriteriaIds = [1023191];
        $rfp->geoTargeting = $geoTargeting;

        $adSize = new AdSize();
        $adSize->width = 300;
        $adSize->height = 260;
        $adSize->type = 'PIXEL';
        $targetedInventorySizes = [$adSize];
        $inventorySizeTargeting = new InventorySizeTargeting();
        $inventorySizeTargeting->targetedInventorySizes = $targetedInventorySizes;
        $rfp->inventorySizeTargeting = $inventorySizeTargeting;

        $fixedPriceAmount = new Money();
        $fixedPriceAmount->currencyCode = 'USD';
        $fixedPriceAmount->units = 1;
        $fixedPriceAmount->nanos = 0;

        $fixedPrice = new Price();
        $fixedPrice->type = "CPM";
        $fixedPrice->amount = $fixedPriceAmount;

        $preferredDealTerms = new PreferredDealTerms();
        $preferredDealTerms->fixedPrice = $fixedPrice;
        $rfp->preferredDealTerms = $preferredDealTerms;

        print "<h2>Sending preferred deal RFP to '$publisherProfileName' on behalf of " .
              "'$parentName':</h2>";
        $result = $this->service->buyers_proposals->sendRfp($parentName, $rfp);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Send RFP for Preferred Deal Proposal';
    }
}

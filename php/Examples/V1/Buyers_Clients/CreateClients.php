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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Clients;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\Client;

/**
 * This example illustrates how to create a client for a given buyer account.
 */
class CreateClients extends BaseExample
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
                    'The resource ID of the buyers resource under which the client is to be ' .
                    'created.',
                'required' => true
            ],
            [
                'name' => 'display_name',
                'display' => 'Display name',
                'description' =>
                    'The display name shown to publishers. Must be unique for clients without ' .
                    'partnerClientId specified. The maximum length allowed is 255 characters. ' .
                    'By default, this sample will specify a generated name.',
                'required' => false,
                'default' => 'Test Client #' . uniqid()
            ],
            [
                'name' => 'partner_client_id',
                'display' => 'Partner client ID',
                'description' =>
                    'Arbitrary unique identifier provided by the buyer. This field can be used ' .
                    'to associate a client with an identifier in the namespace of the buyer. If ' .
                    'present, it must be unique across all the clients. By default, this sample ' .
                    'will not specify a partnerClientId.',
                'required' => false,
                'default' => null
            ],
            [
                'name' => 'role',
                'display' => 'Role',
                'description' =>
                    'The role assigned to the client, which determines its permissions. By ' .
                    'default, this will be set to CLIENT_DEAL_VIEWER. For more details on how ' .
                    'to interpret the different roles, see: https://developers.google.com/' .
                    'authorized-buyers/apis/marketplace/reference/rest/v1/buyers.clients' .
                    '#ClientRole',
                'required' => false,
                'default' => 'CLIENT_DEAL_VIEWER'
            ],
            [
                'name' => 'seller_visible',
                'display' => 'Seller visible',
                'description' =>
                    'Whether the client will be visible to publishers. By default, this sample ' .
                    'will set this to false.',
                'required' => false,
                'default' => false
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

        $newclient = new Client();
        $newclient->displayName = $values['display_name'];
        $newclient->role = $values['role'];
        $newclient->sellerVisible = $values['seller_visible'];

        $partnerClientId = $values['partner_client_id'];
        if (isset($partnerClientId)) {
            $newclient->partnerClientId = $partnerClientId;
        }

        print "<h2>Creating Client for '$parentName':</h2>";
        $result = $this->service->buyers_clients->create($parentName, $newclient);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Create Client';
    }
}

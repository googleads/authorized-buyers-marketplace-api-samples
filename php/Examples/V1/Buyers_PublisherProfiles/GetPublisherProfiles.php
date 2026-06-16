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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_PublisherProfiles;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;

/**
 * This example illustrates how to get a single publisher profile for the given account and profile
 * IDs.
 */
class GetPublisherProfiles extends BaseExample
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
                'required' => true,
                'description' =>
                    'The resource ID of the buyers resource under which the publisherProfiles ' .
                    'resource is being accessed. This will be used to construct the name used ' .
                    'as a path parameter for the publisherProfiles.get request.'
            ],
            [
                'name' => 'publisher_profile_id',
                'display' => 'Publisher Profile ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers.publisherProfiles resource that is being ' .
                    'accessed. This will be used to construct the name used as a path parameter ' .
                    'parameter for the publisherProfiles.get request.'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/publisherProfiles/$values[client_id]";

        try {
            $publisherProfile = $this->service->buyers_publisherProfiles->get($name);
            print '<h2>Found publisher profile.</h2>';
            $this->printResult($publisherProfile);
        } catch (Google_Service_Exception $ex) {
            if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                print '<h1>Publisher profile not found or can\'t be accessed.</h1>';
            } else {
                throw $ex;
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Get Publisher Profile';
    }
}

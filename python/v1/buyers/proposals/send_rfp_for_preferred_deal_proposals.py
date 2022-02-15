#!/usr/bin/python
#
# Copyright 2021 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Sends a request for proposal to a publisher for a preferred deal.

The publisher will be sent an RFP that will initiate negotiation for a
preferred deal. For the buyer, this will create a corresponding proposal.

You must refer to the publisher using their publisher profile. These can be
found with the buyers.publisherProfiles resource.
"""


import argparse
import datetime
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError
import util


_BUYER_NAME_TEMPLATE = 'buyers/%s'
_PUBLISHER_PROFILE_NAME_TEMPLATE = 'buyers/%s/publisherProfiles/%s'

DEFAULT_BUYER_CONTACTS_EMAIL = 'ENTER_BUYER_CONTACTS_EMAIL_HERE'
DEFAULT_BUYER_CONTACTS_DISPLAY_NAME = 'ENTER_BUYER_CONTACTS_DISPLAY_NAME_HERE'
DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_PUB_PROFILE_RESOURCE_ID = 'ENTER_PUB_PROFILE_RESOURCE_ID_HERE'


def main(marketplace, args):
    account_id = args.account_id
    publisher_profile_name = _PUBLISHER_PROFILE_NAME_TEMPLATE % (
        account_id, args.publisher_profile_id)

    flight_start_time = (datetime.datetime.now(datetime.timezone.utc)
                         + datetime.timedelta(days=1))
    flight_end_time = flight_start_time + datetime.timedelta(days=1)

    body = {
        'displayName': args.display_name,
        'publisherProfile': publisher_profile_name,
        'buyerContacts': {
            'email': args.buyer_contacts_email,
            'displayName': args.buyer_contacts_display_name
        },
        'note': 'Test preferred deal proposal created by Python sample.',
        'geoTargeting': {
            # New York, NY
            'targetedCriteriaIds': ['1023191']
        },
        'inventorySizeTargeting': {
            'targetedInventorySizes': [{
                'width': '300',
                'height': '260',
                'type': 'PIXEL'
            }],
        },
        'preferredDealTerms': {
            'fixedPrice': {
                'type': 'CPM',
                'amount': {
                    'currencyCode': 'USD',
                    'units': '1',
                    'nanos': '0'
                }
            }
        },
        'flightStartTime': flight_start_time.isoformat(),
        'flightEndTime': flight_end_time.isoformat()
    }

    print('Sending preferred deal RFP on behalf of buyer account '
          f'"{account_id}" to publisher profile with name '
          f'"{publisher_profile_name}":')
    try:
        # Construct and execute the request.
        response = (marketplace.buyers().proposals().sendRfp(
            buyer=_BUYER_NAME_TEMPLATE % account_id, body=body).execute())
    except HttpError as e:
        print(e)
        sys.exit(1)

    pprint.pprint(response)


if __name__ == '__main__':
    try:
        service = util.get_service(version='v1')
    except IOError as ex:
        print(f'Unable to create marketplace service - {ex}')
        print('Did you specify the key file in util.py?')
        sys.exit(1)

    parser = argparse.ArgumentParser(
        description=('Sends a preferred deal RFP from a given buyer to the '
                     'specified publisher.'))
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource on behalf of which the '
              'RFP is being sent.'))
    parser.add_argument(
        '--buyer_contacts_email', default=DEFAULT_BUYER_CONTACTS_EMAIL,
        help=('Email address for the buyer\'s contact, which will be visible '
              'to the publisher.'))
    parser.add_argument(
       '--buyer_contacts_display_name',
        default=DEFAULT_BUYER_CONTACTS_DISPLAY_NAME,
        help=('The display name of the buyer\'s contact, which will be visible '
              'to the publisher.'))
    parser.add_argument(
        '-p', '--publisher_profile_id', default=DEFAULT_PUB_PROFILE_RESOURCE_ID,
        help=('The resource ID of the publisher profiles resource '
              'representing the publisher that the buyer wants to send the '
              'RFP.'))
    # Optional fields.
    parser.add_argument(
        '-n', '--display_name', default='Test PD Proposal #%s' % uuid.uuid4(),
        help='The display name of the proposal being created by the RFP.')


    main(service, parser.parse_args())

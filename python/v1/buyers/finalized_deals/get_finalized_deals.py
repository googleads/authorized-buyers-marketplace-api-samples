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

"""Gets a single finalized deal for the given account and deal IDs."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_FINALIZED_DEALS_NAME_TEMPLATE = 'buyers/%s/finalizedDeals/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_FINALIZED_DEAL_RESOURCE_ID = 'ENTER_DEAL_RESOURCE_ID_HERE'


def main(marketplace, args):
    finalized_deal_name = _FINALIZED_DEALS_NAME_TEMPLATE % (
        args.account_id, args.deal_id)

    print(f'Get finalized deal with name "{finalized_deal_name}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().finalizedDeals().get(
            name=finalized_deal_name).execute()
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
        description=('Get a finalized deal for the given buyer account ID and '
                     'deal ID.'))
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource for which the finalized '
              'deal is being accessed. This will be used to construct the '
              'name used as a path parameter for the finalizedDeals.get '
              'request.'))
    parser.add_argument(
        '-d', '--deal_id', default=DEFAULT_FINALIZED_DEAL_RESOURCE_ID,
        help=('The resource ID of the buyers.finalizedDeals resource that is '
              'being accessed. Note that this will be identical to the '
              'resource ID of the corresponding buyers.proposals.deals '
              'resource. This will be used to construct the name used as a '
              'path parameter for the finalizedDeals.get request.'))

    main(service, parser.parse_args())

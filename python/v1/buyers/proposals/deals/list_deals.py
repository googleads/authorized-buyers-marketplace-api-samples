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

"""This example lists deals for the specified buyer's proposal."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../../..'))

from googleapiclient.errors import HttpError

import util


_PROPOSAL_NAME_TEMPLATE = 'buyers/%s/proposals/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_PROPOSAL_RESOURCE_ID = 'ENTER_PROPOSAL_RESOURCE_ID_HERE'


def main(marketplace, args):
    proposal_name = _PROPOSAL_NAME_TEMPLATE % (
        args.account_id, args.proposal_id)
    page_size = args.page_size

    page_token = None
    more_pages = True

    print(f'Listing deals for proposal "{proposal_name}":')
    while more_pages:
        try:
            # Construct and execute the request.
            response = marketplace.buyers().proposals().deals().list(
                parent=proposal_name, pageToken=page_token,
                pageSize=page_size).execute()
        except HttpError as e:
            print(e)
            sys.exit(1)

        pprint.pprint(response)

        page_token = response.get('nextPageToken')
        more_pages = bool(page_token)


if __name__ == '__main__':
    try:
        service = util.get_service(version='v1')
    except IOError as ex:
        print(f'Unable to create marketplace service - {ex}')
        print('Did you specify the key file in util.py?')
        sys.exit(1)

    parser = argparse.ArgumentParser(
        description='Lists deals for the given buyer\'s proposal.')
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the deals '
              'were created. This will be used to construct the parent used as '
              'a path parameter for the deals.list request.'))
    parser.add_argument(
        '-p', '--proposal_id', default=DEFAULT_PROPOSAL_RESOURCE_ID,
        help=('The resource ID of the buyers.proposals resource under which '
              'the deals were created. This will be used to construct the name '
              'used as a path parameter for the deals.list request.'))
    # Optional fields.
    parser.add_argument(
        '--page_size', default=util.MAX_PAGE_SIZE,
        help=('The number of rows to return per page. The server may return '
              'fewer rows than specified.'))

    main(service, parser.parse_args())

#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2022 Google LLC
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# Patch the user list targeting of one or more deals for the given proposal.
#
# This operation requires that the deals all exist under the same proposal.
#
# The user list targeting of the given deals will be modified to target the
# specified user lists. User lists can be retrieved via the Real-time Bidding
# API's buyers.userLists resource. You can learn more about buyers.userLists in
# the reference documentation:
# https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.userLists
#
# Note: Only preferred and programmatic guaranteed deals can be modified by the
# buyer; attempting to modify a private auction deal will result in an error
# response.

require 'optparse'

require_relative '../../../../util'


def batch_update_deals(marketplace, options)
  parent = "buyers/#{options[:account_id]}/proposals/#{options[:proposal_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::BatchUpdateDealsRequest.new(
    requests: options[:deal_ids].map {
      |deal_id|
      Google::Apis::AuthorizedbuyersmarketplaceV1::UpdateDealRequest.new(
        update_mask: 'targeting.userListTargeting.targetedCriteriaIds',
        deal: Google::Apis::AuthorizedbuyersmarketplaceV1::Deal.new(
          name: "#{parent}/deals/#{deal_id}",
          proposal_revision: options[:proposal_revision],
          targeting: Google::Apis::AuthorizedbuyersmarketplaceV1::MarketplaceTargeting.new(
            user_list_targeting: Google::Apis::AuthorizedbuyersmarketplaceV1::CriteriaTargeting.new(
              targeted_criteria_ids: options[:user_list_ids]
            )
          )
        )
      )
    }.compact
  )

  puts "Batch updating deals for proposal with name '#{parent}':"

  response = marketplace.batch_update_deals(parent, body)

  response.deals.each do |deal|
    DealPrinter.print(deal)
  end
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise 'Unable to create service, was the KEY_FILE in util.rb set? Error message: #{e.message}'
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which one or more deals are being patched. This will be used to '\
      'construct the proposal name used as a path parameter for the deals.batchUpdate request, and each deal name '\
      'included in the request body.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'proposal_id',
      'The resource ID of the buyers.proposals resource under which one or more deals are being patched. This will '\
      'be used to construct the proposal name used as a path parameter for the deals.batchUpdate request, and each '\
      'deal name included in the request body.',
      short_alias: 'p', required: true, default_value: nil
    ),
    Option.new(
      'deal_ids',
      'One or more resource IDs for the buyers.proposals.deals resource that will be patched in a batch update '\
      'operation. This will be used to construct the deal name that is included in the update request for each deal '\
      'in the deals.batchUpdate request. Specify each ID separated by a comma.',
      type: Array, short_alias: 'd', required: true
    ),
    Option.new(
      'proposal_revision',
      'The revision number for the parent proposal of the deals being modified. Each update to the proposal or its '\
      'deals causes the number to increment. The revision number specified must match the value stored server-side '\
      'in order for the operation to be performed.',
      short_alias: 'r', required: true, default_value: nil
    ),
    Option.new(
      'user_list_ids',
      'The resource ID of one or more buyers.userLists resources that are to be targeted by the given deals. Specify '\
      'each ID separated by a comma.',
      type: Array, short_alias: 'u', required: true
    )
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    batch_update_deals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end

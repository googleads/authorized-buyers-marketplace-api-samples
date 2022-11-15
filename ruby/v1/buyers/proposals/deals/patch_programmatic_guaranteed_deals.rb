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
# Patches a programmatic guaranteed deal at the given revision number.
#
# This will modify the deal's flightStartTime, flightEndTime, and
# programmaticGuaranteedTerms.
#
# Note: If the revision number is lower than what is stored for the proposal
# server-side, the operation will be deemed obsolete and an error will be
# returned. The revision number can be found at the proposal level.

require 'optparse'
require 'securerandom'

require_relative '../../../../util'


def patch_programmatic_guaranteed_deals(marketplace, options)
  name = "buyers/#{options[:account_id]}/proposals/#{options[:proposal_id]}/deals/#{options[:deal_id]}"

  flight_start_time = DateTime.now() + 1
  flight_end_time = flight_start_time + 1

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::Deal.new(
    proposal_revision: options[:proposal_revision],
    flight_start_time: flight_start_time.rfc3339,
    flight_end_time: flight_end_time.rfc3339,
    preferred_deal_terms: Google::Apis::AuthorizedbuyersmarketplaceV1::ProgrammaticGuaranteedTerms.new(
      fixed_price: Google::Apis::AuthorizedbuyersmarketplaceV1::Price.new(
        amount: Google::Apis::AuthorizedbuyersmarketplaceV1::Money.new(
          units: options[:fixed_price_units],
          nanos: options[:fixed_price_nanos]
        )
      )
    )
  )

  update_mask = 'flightStartTime,flightEndTime,programmaticGuaranteedTerms.fixedPrice.amount.units,'\
                'programmaticGuaranteedTerms.fixedPrice.amount.nanos'

  puts "Patching deal with name '#{name}':"

  patched_deal = marketplace.patch_buyer_proposal_deal(
      name, body, update_mask: update_mask)
  DealPrinter.print(patched_deal)
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
      'The resource ID of the buyers resource under which the deal is being patched. This will be used to construct '\
      'the name used as a path parameter for the deals.patch request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'proposal_id',
      'The resource ID of the buyers.proposals resource under which the deal is being patched. This will be used to '\
      'construct the name used as a path parameter for the deals.patch request.',
      short_alias: 'p', required: true, default_value: nil
    ),
    Option.new(
      'deal_id',
      'The resource ID of the buyers.proposals.deals resource that is being patched. This will be used to construct '\
      'the name used as a path parameter for the deals.patch request.',
      short_alias: 'd', required: true, default_value: nil
    ),
    Option.new(
      'proposal_revision',
      'The revision number for the parent proposal being modified. Each update to the proposal or its deals causes '\
      'the number to increment. The revision number specified must match the value stored server-side in order for '\
      'the operation to be performed.',
      short_alias: 'r', required: true, default_value: nil
    ),
    Option.new(
      'fixed_price_units',
      'Whole units of the currency specified for the programmatic guaranteed deal.',
      type: Integer, short_alias: 'u', required: false, default_value: 1
    ),
    Option.new(
      'fixed_price_nanos',
      'Number of nano units of the currency specified for the programmatic guaranteed deal.',
      type: Integer, short_alias: 'n', required: false, default_value: 500000000
    )
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    patch_programmatic_guaranteed_deals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end

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
# Sends a request for proposal (RFP) to a publisher for a programmatic
# guaranteed deal.
#
# The publisher will be sent an RFP that will initiate negotiation for a
# programmatic guaranteed deal. For the buyer, this will create a corresponding
# proposal.
#
# You must refer to the publisher using their publisher profile. These can be
# found with the buyers.publisherProfiles resource.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def send_rfp_for_programmatic_guaranteed_deal_proposals(marketplace, options)
  parent = "buyers/#{options[:account_id]}"
  publisher_profile_name = "#{parent}/publisherProfiles/#{options[:publisher_profile_id]}"

  flight_start_time = DateTime.now()
  flight_end_time = flight_start_time + 1

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::SendRfpRequest.new(
    display_name: options[:display_name],
    publisher_profile: publisher_profile_name,
    buyer_contacts: [Google::Apis::AuthorizedbuyersmarketplaceV1::Contact.new(
      email: options[:buyer_contacts_email],
      display_name: options[:buyer_contacts_display_name]
    )],
    note: 'Test programmatic guaranteed deal proposal created by Ruby sample.',
    geo_targeting: Google::Apis::AuthorizedbuyersmarketplaceV1::CriteriaTargeting.new(
      # Target New York, NY
      targeted_criteria_ids: [1023191]
    ),
    inventory_size_targeting: Google::Apis::AuthorizedbuyersmarketplaceV1::InventorySizeTargeting.new(
      targeted_inventory_sizes: [
        Google::Apis::AuthorizedbuyersmarketplaceV1::AdSize.new(
          width: 300,
          height: 260,
          type: 'PIXEL'
        )
      ]
    ),
    # Specify the start and end flight times in RFC3339 UTC "Zulu" format.
    flight_start_time: flight_start_time.rfc3339,
    flight_end_time: flight_end_time.rfc3339,
    programmatic_guaranteed_terms: Google::Apis::AuthorizedbuyersmarketplaceV1::ProgrammaticGuaranteedTerms.new(
      guaranteed_looks: 0,
      fixed_price: Google::Apis::AuthorizedbuyersmarketplaceV1::Price.new(
        type: "CPM",
        amount: Google::Apis::AuthorizedbuyersmarketplaceV1::Money.new(
          currency_code: "USD",
          units: 1,
          nanos: 0
        )
      ),
      minimum_daily_looks: 0,
      reservation_type: 'STANDARD',
      impressionCap: 0,
      percent_share_of_voice: 0
    )
  )

  puts "Creating programmatic guaranteed deal RFP on behalf of buyer account '#{parent}' to publisher profile with "\
       "name #{publisher_profile_name}"

  programmatic_guaranteed_deal_proposal = marketplace.send_proposal_rfp(parent, body)
  ProposalPrinter.print(programmatic_guaranteed_deal_proposal)
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
      'The resource ID of the buyers resource on behalf of which the RFP is being sent.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'buyer_contacts_display_name',
      'The display name of the buyer\'s contact, which will be visible to the publisher.',
      short_alias: 'n', required: true, default_value: nil
    ),
    Option.new(
      'buyer_contacts_email',
      'Email address for the buyer\'s contact, which will be visible to the publisher.',
      short_alias: 'e', required: true, default_value: nil
    ),
    Option.new(
      'publisher_profile_id',
      'The resource ID of the publisher profile representing the publisher that the buyer wants to send an RFP.',
      short_alias: 'p', required: true, default_value: nil
    ),
    Option.new(
      'display_name', 'The display name of the proposal being created by the RFP.',
      short_alias: 'd', required: false, default_value: "Test PG Proposal #{SecureRandom.uuid}"
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    send_rfp_for_programmatic_guaranteed_deal_proposals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end

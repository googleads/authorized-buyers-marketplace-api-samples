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
# Common utilities used by the Authorized Buyers Marketplace API samples.

require 'optparse'
require 'google/apis/authorizedbuyersmarketplace_v1'
require 'google/apis/options'
require 'googleauth/service_account'


MARKETPLACE_V1 = 'v1'
DEFAULT_VERSION = MARKETPLACE_V1
SUPPORTED_RTB_API_VERSIONS = [MARKETPLACE_V1]

# The JSON key file for your Service Account found in the Google Developers
# Console.
KEY_FILE = 'path_to_key'  # Path to JSON file containing your private key.

# The maximum number of results to be returned in a page for any list response.
MAX_PAGE_SIZE = 50


# Handles authentication and initializes the client.
def get_service(version=DEFAULT_VERSION)
  if !SUPPORTED_RTB_API_VERSIONS.include? version
    raise ArgumentError, 'Unsupported version (#{version}) of the Marketplace API specified!'
  end

  Google::Apis::ClientOptions.default.application_name =
      "Ruby Marketplace API samples: #{$0}"
  Google::Apis::ClientOptions.default.application_version = "1.0.0"

  case version
  when MARKETPLACE_V1
    service = Google::Apis::AuthorizedbuyersmarketplaceV1::AuthorizedBuyersMarketplaceService.new
  end

  auth_options = {
    :json_key_io => File.open(KEY_FILE, "r"),
    :scope => "https://www.googleapis.com/auth/authorized-buyers-marketplace"
  }

  authorization = Google::Auth::ServiceAccountCredentials.make_creds(
      options=auth_options)
  service.authorization = authorization
  service.authorization.fetch_access_token!

  return service
end


# Base class for utilities used to print Marketplace API resources.
class MarketplaceResourcePrinter

  protected

  def self.get_prefix(indent_level)
    bullet = indent_level == 0 ? "*" : "-"
    return "#{' ' * (indent_level* 2)} #{bullet} "
  end

  def self.print_field(field_desc, field, indent_level, default_value=nil)
    unless field.nil? and default_value.nil?
      prefix = self.get_prefix(indent_level)
      value = field.nil? ? default_value : field
      puts "#{prefix}#{field_desc}: #{value}"
    end
  end

  def self.print_list(field_desc, values, indent_level)
    unless values.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"

      item_prefix = self.get_prefix(indent_level + 1)

      values.each do |item|
        puts "#{item_prefix}#{item}"
      end
    end
  end
end


# Utility used to print Marketplace API's auction package resource.
class AuctionPackagePrinter < MarketplaceResourcePrinter

  def self.print(auction_package)
    indent_level = 0

    self.print_field("Auction package name", auction_package.name, indent_level)

    indent_level += 1
    self.print_field("Display name", auction_package.display_name, indent_level)
    self.print_field("Description", auction_package.description, indent_level)
    self.print_field("Creator", auction_package.creator, indent_level)
    self.print_field("Create time", auction_package.create_time, indent_level)
    self.print_field("Update time", auction_package.update_time, indent_level)
    self.print_list("Subscribed clients", auction_package.subscribed_clients, indent_level)
  end
end


# Utility used to print Marketplace API's client resource.
class ClientPrinter < MarketplaceResourcePrinter

  def self.print(client)
    indent_level = 0

    self.print_field("Client name", client.name, indent_level)

    indent_level += 1
    self.print_field("Display name", client.display_name, indent_level)
    self.print_field("Partner client ID", client.partner_client_id, indent_level)
    self.print_field("Role", client.role, indent_level)
    self.print_field("State", client.state, indent_level)
    self.print_field("Seller visible", client.seller_visible, indent_level)
  end
end


# Utility used to print Marketplace API's client user resource.
class ClientUserPrinter < MarketplaceResourcePrinter

  def self.print(client_user)
    indent_level = 0

    self.print_field("Client user name", client_user.name, indent_level)

    indent_level += 1
    self.print_field("State", client_user.state, indent_level)
    self.print_field("Email", client_user.email, indent_level)
  end
end


# Utility used to print Marketplace API's deal resource.
class DealPrinter < MarketplaceResourcePrinter

  def self.print(deal)
    self.print_deal(deal, 0)
  end

  protected

  def self.print_deal(deal, indent_level)
    self.print_field("Deal name", deal.name, indent_level)

    indent_level += 1
    self.print_field("Create time", deal.update_time, indent_level)
    self.print_field("Update time", deal.update_time, indent_level)
    self.print_field("Proposal revision", deal.proposal_revision, indent_level)
    self.print_field("Display name", deal.display_name, indent_level)
    self.print_field("Billed buyer", deal.billed_buyer, indent_level)
    self.print_field("Publisher profile", deal.publisher_profile, indent_level)
    self.print_field("Deal type", deal.deal_type, indent_level)
    self.print_money("Estimated gross spend", deal.estimated_gross_spend, indent_level)
    self.print_time_zone("Seller time zone", deal.seller_time_zone, indent_level)
    self.print_field("Description", deal.description, indent_level)
    self.print_field("Flight start time", deal.flight_start_time, indent_level)
    self.print_field("Flight end time", deal.flight_end_time, indent_level)
    self.print_marketplace_targeting(deal.targeting, indent_level)
    self.print_creative_requirements(deal.creative_requirements, indent_level)
    self.print_delivery_control(deal.delivery_control, indent_level)
    self.print_field("Buyer", deal.buyer, indent_level)
    self.print_field("Client", deal.client, indent_level)
    self.print_programmatic_guaranteed_terms(deal.programmatic_guaranteed_terms, indent_level)
    self.print_preferred_deal_terms(deal.preferred_deal_terms, indent_level)
    self.print_private_auction_terms(deal.private_auction_terms, indent_level)
  end

  private

  def self.print_money(field_desc, money, indent_level)
    unless money.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"
      indent_level += 1
      self.print_field("Currency code", money.currency_code, indent_level)
      self.print_field("Units", money.units, indent_level)
      self.print_field("Nanos", money.nanos, indent_level)
    end
  end

  def self.print_time_zone(field_desc, time_zone, indent_level)
    unless time_zone.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"
      indent_level += 1
      self.print_field("ID", time_zone.id, indent_level)
      self.print_field("Version", time_zone.version, indent_level)
    end
  end

  def self.print_marketplace_targeting(marketplace_targeting, indent_level)
    unless marketplace_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Targeting:"
      indent_level += 1
      self.print_criteria_targeting("Geo targeting", marketplace_targeting.geo_targeting, indent_level)
      self.print_inventory_size_targeting(marketplace_targeting.inventory_size_targeting, indent_level)
      self.print_technology_targeting(marketplace_targeting.technology_targeting, indent_level)
      self.print_placement_targeting(marketplace_targeting.placement_targeting, indent_level)
      self.print_video_targeting(marketplace_targeting.video_targeting, indent_level)
      self.print_criteria_targeting("User list targeting", marketplace_targeting.user_list_targeting, indent_level)
      self.print_daypart_targeting(marketplace_targeting.daypart_targeting, indent_level)
    end
  end

  def self.print_criteria_targeting(field_desc, criteria_targeting, indent_level)
    unless criteria_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"
      indent_level += 1
      self.print_list("Targeted criteria IDs", criteria_targeting.targeted_criteria_ids, indent_level)
      self.print_list("Excluded Criteria IDs", criteria_targeting.excluded_criteria_ids, indent_level)
    end
  end

  def self.print_inventory_size_targeting(inventory_size_targeting, indent_level)
    unless inventory_size_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Inventory size targeting:"
      indent_level += 1
      self.print_ad_size_list(
        "Targeted inventory sizes", inventory_size_targeting.targeted_inventory_sizes, indent_level)
      self.print_ad_size_list(
        "Excluded inventory sizes", inventory_size_targeting.excluded_inventory_sizes, indent_level)
    end
  end

  def self.print_ad_size(ad_size, indent_level)
    unless ad_size.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{ad_size.type}:"
      indent_level += 1
      self.print_field("Width", ad_size.width, indent_level)
      self.print_field("Height", ad_size.height, indent_level)
    end
  end

  def self.print_ad_size_list(field_desc, ad_sizes, indent_level)
    unless ad_sizes.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"

      indent_level += 1

      ad_sizes.each do |ad_size|
        self.print_ad_size(ad_size, indent_level)
      end
    end
  end

  def self.print_technology_targeting(technology_targeting, indent_level)
    unless technology_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Technology targeting:"
      indent_level += 1
      self.print_criteria_targeting(
        "Device category targeting", technology_targeting.device_category_targeting, indent_level)
      self.print_criteria_targeting(
        "Device capability targeting", technology_targeting.device_capability_targeting, indent_level)
      self.print_operating_system_targeting(technology_targeting.operating_system_targeting, indent_level)
    end
  end

  def self.print_operating_system_targeting(operating_system_targeting, indent_level)
    unless operating_system_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Operating system targeting:"
      indent_level += 1
      self.print_criteria_targeting(
        "Operating system criteria", operating_system_targeting.operating_system_criteria, indent_level)
      self.print_criteria_targeting(
        "Operating system version criteria",
        operating_system_targeting.operating_system_version_criteria,
        indent_level
      )
    end
  end

  def self.print_placement_targeting(placement_targeting, indent_level)
    unless placement_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Placement targeting:"
      indent_level += 1
      self.print_uri_targeting(placement_targeting.uri_targeting, indent_level)
      self.print_mobile_application_targeting(placement_targeting.mobile_application_targeting, indent_level)
    end
  end

  def self.print_uri_targeting(uri_targeting, indent_level)
    unless uri_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}URI targeting:"
      indent_level += 1
      self.print_list("Targeted URIs", uri_targeting.targeted_uris, indent_level)
      self.print_list("Excluded URIs", uri_targeting.excluded_uris, indent_level)
    end
  end

  def self.print_mobile_application_targeting(mobile_application_targeting, indent_level)
    unless mobile_application_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Mobile application targeting:"
      indent_level += 1
      self.print_first_party_mobile_application_targeting(
        "First-party targeting", uri_targeting.targeted_uris, indent_level)
    end
  end

  def self.print_first_party_mobile_application_targeting(
      field_desc, first_party_mobile_application_targeting, indent_level)
    unless first_party_mobile_application_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"
      indent_level += 1
      self.print_list("Targeted app IDs", first_party_mobile_application_targeting.targeted_app_ids, indent_level)
      self.print_list("Excluded app IDs", first_party_mobile_application_targeting.excluded_app_ids, indent_level)
    end
  end

  def self.print_video_targeting(video_targeting, indent_level)
    unless video_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Video targeting:"
      indent_level += 1
      self.print_list("Targeted position types", video_targeting.targeted_position_types, indent_level)
      self.print_list("Excluded position types", video_targeting.excluded_position_types, indent_level)
    end
  end

  def self.print_daypart_targeting(daypart_targeting, indent_level)
    unless daypart_targeting.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Day part targeting:"
      indent_level += 1
      self.print_daypart_list(daypart_targeting.day_parts, indent_level)
      self.print_field("Time zone type", daypart_targeting.time_zone_type, indent_level)
    end
  end

  def self.print_daypart(daypart, indent_level)
    unless daypart.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{daypart.day_of_week}:"
      indent_level += 1
      self.print_time_of_day("Start time", daypart.start_time, indent_level)
      self.print_time_of_day("End time", daypart.end_time, indent_level)
    end
  end

  def self.print_time_of_day(field_desc, time_of_day, indent_level)
    unless time_of_day.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"
      indent_level += 1
      self.print_field("Hours", time_of_day.hours, indent_level)
      self.print_field("Minutes", time_of_day.minutes, indent_level)
      self.print_field("Seconds", time_of_day.seconds, indent_level)
      self.print_field("Nanos", time_of_day.nanos, indent_level)
    end
  end

  def self.print_daypart_list(dayparts, indent_level)
    unless dayparts.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Dayparts:"

      indent_level += 1

      dayparts.each do |daypart|
        self.print_daypart(daypart, indent_level)
      end
    end
  end

  def self.print_creative_requirements(creative_requirements, indent_level)
    unless creative_requirements.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Creative requirements:"
      indent_level += 1
      self.print_field("Creative preapproval policy", creative_requirements.creative_pre_approval_policy, indent_level)
      self.print_field(
        "Creative safeframe compatibility",
        creative_requirements.creative_safe_frame_compatibility,
        indent_level
      )
      self.print_field("Programmatic creative source", creative_requirements.programmatic_creative_source, indent_level)
      self.print_field("Creative format", creative_requirements.creative_format, indent_level)
      self.print_field("Skippable ad type", creative_requirements.skippable_ad_type, indent_level)
      self.print_field("Max duration ms", creative_requirements.max_ad_duration_ms, indent_level)
    end
  end

  def self.print_delivery_control(delivery_control, indent_level)
    unless delivery_control.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Delivery control:"
      indent_level += 1
      self.print_field("Delivery rate type", delivery_control.delivery_rate_type, indent_level)
      self.print_frequency_cap_list(delivery_control.frequency_cap, indent_level)
      self.print_field("Road blocking type", delivery_control.roadblocking_type, indent_level)
      self.print_field("companion_delivery_type", delivery_control.companion_delivery_type, indent_level)
      self.print_field("creative_rotation_type", delivery_control.creative_rotation_type, indent_level)
    end
  end

  def self.print_frequency_cap(frequency_cap, indent_level)
    unless frequency_cap.nil?
      prefix = self.get_prefix(indent_level)
      self.print_field("Max impressions", frequency_cap.max_impressions, indent_level)
      indent_level += 1
      self.print_field("Time units count", frequency_cap.time_units_count, indent_level)
      self.print_field("Time unit type", frequency_cap.time_unit_type, indent_level)
    end
  end

  def self.print_frequency_cap_list(frequency_caps, indent_level)
    unless frequency_caps.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Frequency cap:"

      indent_level += 1

      frequency_caps.each do |frequency_cap|
        self.print_frequency_cap(frequency_cap, indent_level)
      end
    end
  end

  def self.print_programmatic_guaranteed_terms(programmatic_guaranteed_terms, indent_level)
    unless programmatic_guaranteed_terms.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Programmatic guaranteed terms:"
      indent_level += 1
      self.print_field("Guaranteed looks", programmatic_guaranteed_terms.guaranteed_looks, indent_level)
      self.print_price("Fixed price", programmatic_guaranteed_terms.fixed_price, indent_level)
      self.print_field("Minimum daily looks", programmatic_guaranteed_terms.minimum_daily_looks, indent_level)
      self.print_field("Reservation type", programmatic_guaranteed_terms.reservation_type, indent_level)
      self.print_field("Impression cap", programmatic_guaranteed_terms.impression_cap, indent_level)
      self.print_field("Percent share of voice", programmatic_guaranteed_terms.percent_share_of_voice, indent_level)
    end
  end

  def self.print_price(field_desc, price, indent_level)
    unless price.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"
      indent_level += 1
      self.print_field("Type", price.type, indent_level)
      self.print_money("Amount", price.amount, indent_level)
    end
  end

  def self.print_preferred_deal_terms(preferred_deal_terms, indent_level)
    unless preferred_deal_terms.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Preferred deal terms:"
      indent_level += 1
      self.print_price("Fixed price", programmatic_guaranteed_terms.fixed_price, indent_level)
    end
  end

  def self.print_private_auction_terms(private_auction_terms, indent_level)
    unless private_auction_terms.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Private auction terms:"
      indent_level += 1
      self.print_price("Floor price", private_auction_terms.floor_price, indent_level)
      self.print_field("Open auction allowed", private_auction_terms.open_auction_allowed, indent_level)
    end
  end
end


# Utility used to print Marketplace API's finalizedDeal resource.
class FinalizedDealPrinter < DealPrinter

  def self.print(finalized_deal)
    indent_level = 0

    self.print_field("Finalized deal name", finalized_deal.name, indent_level)

    indent_level += 1
    self.print_deal(finalized_deal.deal, indent_level)
    self.print_field("Deal serving status", finalized_deal.deal_serving_status, indent_level)
    self.print_deal_pausing_info(finalized_deal.deal_pausing_info, indent_level)
    self.print_rtb_metrics(finalized_deal.rtb_metrics, indent_level)
    self.print_field("Ready to serve", finalized_deal.ready_to_serve, indent_level)
  end

  def self.print_deal_pausing_info(deal_pausing_info, indent_level)
    unless deal_pausing_info.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}Deal pausing info:"
      indent_level += 1
      self.print_field("Pausing consented", deal_pausing_info.pausing_consented, indent_level)
      self.print_field("Pause role", deal_pausing_info.pause_role, indent_level)
      self.print_field("Pause reason", deal_pausing_info.pause_reason, indent_level)
    end
  end

  def self.print_rtb_metrics(rtb_metrics, indent_level)
    unless rtb_metrics.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}RTB metrics:"
      indent_level += 1
      self.print_field("Bid requests 7 days", rtb_metrics.bid_requests7_days, indent_level)
      self.print_field("Bids 7 days", rtb_metrics.bids7_days, indent_level)
      self.print_field("Ad impressions 7 days", rtb_metrics.ad_impressions7_days, indent_level)
      self.print_field("Bid rate 7 days", rtb_metrics.bid_rate7_days, indent_level)
      self.print_field("Filtered bid rate 7 days", rtb_metrics.filtered_bid_rate7_days, indent_level)
      self.print_field("Must bid rate current month", rtb_metrics.must_bid_rate_current_month, indent_level)
    end
  end
end


# Utility used to print Marketplace API's proposal resource.
class ProposalPrinter < MarketplaceResourcePrinter

  def self.print(proposal)
    indent_level = 0

    self.print_field("Proposal name", proposal.name, indent_level)

    indent_level += 1
    self.print_field("Update time", proposal.update_time, indent_level)
    self.print_field("Proposal revision", proposal.proposal_revision, indent_level)
    self.print_field("Deal type", proposal.deal_type, indent_level)
    self.print_field("Display name", proposal.display_name, indent_level)
    self.print_field("State", proposal.state, indent_level)
    self.print_field("Is renegotiating", proposal.is_renegotiating, indent_level)
    self.print_field("Originator role", proposal.originator_role, indent_level)
    self.print_field("Publisher profile", proposal.publisher_profile, indent_level)
    self.print_private_data("Buyer private data", proposal.buyer_private_data, indent_level)
    self.print_field("Billed buyer", proposal.billed_buyer, indent_level)
    self.print_contact_list("Seller contacts", proposal.seller_contacts, indent_level)
    self.print_contact_list("Buyer contacts", proposal.buyer_contacts, indent_level)
    self.print_field("lastUpdaterOrCommentorRole", proposal.last_updater_or_commentor_role, indent_level)
    self.print_field("Terms and conditions", proposal.terms_and_conditions, indent_level)
    self.print_field("Pausing consented", proposal.pausing_consented, indent_level)
    self.print_note_list("Notes", proposal.notes, indent_level)
    self.print_field("Buyer", proposal.buyer, indent_level)
    self.print_field("Client", proposal.client, indent_level)
  end

  private

  def self.print_private_data(field_desc, private_data, indent_level)
    unless private_data.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"
      self.print_field("Reference ID", private_data.reference_id, indent_level + 1)
    end
  end

  def self.print_contact(contact, indent_level)
    prefix = self.get_prefix(indent_level)
    puts "#{prefix}#{contact.email}:"
    self.print_field("Display name", contact.display_name, indent_level + 1)
  end

  def self.print_contact_list(field_desc, contacts, indent_level)
    unless contacts.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"

      indent_level += 1

      contacts.each do |contact|
        self.print_contact(contact, indent_level)
      end
    end
  end

  def self.print_note(note, indent_level)
    prefix = self.get_prefix(indent_level)
    puts "#{prefix}#{note.create_time}:"
    indent_level += 1
    self.print_field("Creator role", note.creator_role, indent_level)
    self.print_field("Note", note.note, indent_level)
  end

  def self.print_note_list(field_desc, notes, indent_level)
    unless notes.nil?
      prefix = self.get_prefix(indent_level)
      puts "#{prefix}#{field_desc}:"

      indent_level += 1

      notes.each do |note|
        self.print_note(note, indent_level)
      end
    end
  end
end


# Utility used to print Marketplace API's publisher profile resource.
class PublisherProfilePrinter < MarketplaceResourcePrinter

  def self.print(publisher_profile)
    indent_level = 0

    self.print_field("Publisher profile name", publisher_profile.name, indent_level)

    indent_level += 1
    self.print_field("Display name", publisher_profile.display_name, indent_level)
    self.print_list("Domains", publisher_profile.domains, indent_level)
    self.print_list("Mobile apps", publisher_profile.mobile_apps, indent_level)
    self.print_field("Logo URL", publisher_profile.logo_url, indent_level)
    self.print_field("Direct deals contact", publisher_profile.direct_deals_contact, indent_level)
    self.print_field("Programmatic deals contact", publisher_profile.programmatic_deals_contact, indent_level)
    self.print_field("Media kit URL", publisher_profile.media_kit_url, indent_level)
    self.print_field("Sample page URL", publisher_profile.sample_page_url, indent_level)
    self.print_field("Overview", publisher_profile.overview, indent_level)
    self.print_field("Pitch statement", publisher_profile.pitch_statement, indent_level)
    self.print_list("Top headlines", publisher_profile.top_headlines, indent_level)
    self.print_field("Audience description", publisher_profile.audience_description, indent_level)
    self.print_field("Is parent", publisher_profile.is_parent, indent_level)
    self.print_field("Publisher code", publisher_profile.publisher_code, indent_level)
  end
end


# An option to be passed into the example via a command-line argument.
class Option

  # The long alias for the option; typically one or more words delimited by
  # underscores. Do not use "--help", as this is reserved for displaying help
  # information.
  attr_reader :long_alias

  # The symbol associated with this option when parsed; this is the long alias
  # converted to a symbol, and will be used to access the parsed value for the
  # option.
  attr_reader :symbol

  # The type used for type coercion.
  attr_reader :type

  # The text displayed for the option if the user passes the "-h" or "--help"
  # options to display help information for the sample.
  attr_reader :help_text

  # The short alias for the option; a single character. Do not use "-h", as
  # this is reserved for displaying help information.
  attr_reader :short_alias

  # An optional array of values to be used to validate a user-specified value
  # against.
  attr_reader :valid_values

  # The default value to use if the option is not specified as a command-line
  # argument.
  attr_reader :default_value

  # A boolean indicating whether it is required that the user configure the
  # option.
  attr_reader :required

  def initialize(long_alias, help_template, type: String,
      short_alias: nil, valid_values: nil, required: false,
      default_value: nil)

    @long_alias = long_alias
    @symbol = long_alias.to_sym

    if valid_values.nil?
      @help_text = help_template
    elsif !valid_values.kind_of?(Array)
      raise ArgumentError, 'The valid_values argument must be an Array.'
    else
      @valid_values = valid_values.map(&:upcase)
      @help_text =
        help_template + " This can be set to: #{@valid_values.inspect}."
    end

    @type = type
    @short_alias = short_alias
    @default_value = default_value
    @required = required
  end

  def get_option_parser_args()
    args = []

    unless short_alias.nil?
      args << "-#{@short_alias} #{@long_alias.upcase}"
    end

    args << "--#{@long_alias} #{@long_alias.upcase}"
    args << @type
    args << @help_text

    return args
  end
end


# Parses arguments for the given Options.
class Parser
  def initialize(options)
    @parsed_args = {}
    @options = options
    @opt_parser = OptionParser.new do |opts|
      options.each do |option|
        opts.on(*option.get_option_parser_args()) do |x|
          unless option.valid_values.nil?
            if option.kind_of?(Array)
              x.each do |value|
                check_valid_value(option.valid_values, value)
              end
            else
              check_valid_value(option.valid_values, x)
            end
          end
          @parsed_args[option.symbol] = x
        end
      end
    end
  end

  def check_valid_value(valid_values, value)
    unless valid_values.include?(value.upcase)
      raise "Invalid value '#{value}'. Valid values are: '#{valid_values.inspect}'"
    end
  end

  def parse(args)
    @opt_parser.parse!(args)

    @options.each do |option|
      if !@parsed_args.include? option.symbol
        @parsed_args[option.symbol] = option.default_value
      end

      if option.required and @parsed_args[option.symbol].nil?
        raise "You need to set '#{option.long_alias}', it is a required field. Set it by passing "\
              "'--#{option.long_alias} #{option.long_alias.upcase}' as a command line argument or giving the "\
              "corresponding option a default value."
      end
    end

    return @parsed_args
  end
end

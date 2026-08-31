/**
 * Manual service for issuer issuance analytics.
 * Consumes the endpoints defined in SPECS/org-issuance-reporting.md.
 * To be replaced by orval-generated code once the backend implements the endpoints.
 */
import type { Uuid } from '../../models';

export type AssertionStatus = 'CLAIMED' | 'PENDING' | 'REVOKED';

export interface BadgeIssuanceSummary {
  badgeId?: Uuid;
  name?: string;
  imageUrl?: string;
  issued?: number;
  claimed?: number;
  pending?: number;
  revoked?: number;
  claimRate?: number;
}

export interface BadgeAssertionRecipient {
  fullName?: string;
  email?: string;
}

export interface BadgeAssertionItem {
  assertionId?: Uuid;
  recipient?: BadgeAssertionRecipient;
  status?: AssertionStatus;
  issuedOn?: string;
  evidence?: string;
  isPublic?: boolean;
}

export interface PaginationMeta {
  totalRecords?: number;
  currentPage?: number;
  totalPages?: number;
  nextPage?: number;
  prevPage?: number;
}

export interface Paginated<T> {
  data?: T[];
  meta?: PaginationMeta;
}

export interface GetApiV2IssuersIssuerUuidBadgesAnalyticsParams {
  page?: number;
  size?: number;
}

export interface GetApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertionsParams {
  page?: number;
  size?: number;
  sort?: string;
  status?: AssertionStatus;
  search?: string;
  from?: string;
  to?: string;
}

const getUrl = (path: string, params?: Record<string, unknown>): string => {
  const search = new URLSearchParams();
  Object.entries(params || {}).forEach(([key, value]) => {
    if (value !== undefined && value !== null) search.append(key, String(value));
  });
  const qs = search.toString();
  return qs ? `${path}?${qs}` : path;
};

export const getGetApiV2IssuersIssuerUuidBadgesAnalyticsUrl = (
  issuerUuid: Uuid,
  params?: GetApiV2IssuersIssuerUuidBadgesAnalyticsParams,
) => getUrl(`/api/v2/issuers/${issuerUuid}/badges/analytics`, params);

export const getApiV2IssuersIssuerUuidBadgesAnalytics = async (
  issuerUuid: Uuid,
  params?: GetApiV2IssuersIssuerUuidBadgesAnalyticsParams,
  options?: RequestInit,
): Promise<{ data: Paginated<BadgeIssuanceSummary>; status: number }> => {
  const res = await fetch(getGetApiV2IssuersIssuerUuidBadgesAnalyticsUrl(issuerUuid, params), {
    ...options,
    method: 'GET',
  });
  const body = [204, 205, 304].includes(res.status) ? null : await res.text();
  const data = body ? JSON.parse(body) : {};
  return { data, status: res.status };
};

export const getGetApiV2IssuersIssuerUuidBadgesBadgeClassUuidAnalyticsUrl = (
  issuerUuid: Uuid,
  badgeClassUuid: Uuid,
) => `/api/v2/issuers/${issuerUuid}/badges/${badgeClassUuid}/analytics`;

export const getApiV2IssuersIssuerUuidBadgesBadgeClassUuidAnalytics = async (
  issuerUuid: Uuid,
  badgeClassUuid: Uuid,
  options?: RequestInit,
): Promise<{ data: BadgeIssuanceSummary; status: number }> => {
  const res = await fetch(getGetApiV2IssuersIssuerUuidBadgesBadgeClassUuidAnalyticsUrl(issuerUuid, badgeClassUuid), {
    ...options,
    method: 'GET',
  });
  const body = [204, 205, 304].includes(res.status) ? null : await res.text();
  const data = body ? JSON.parse(body) : {};
  return { data, status: res.status };
};

export const getGetApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertionsUrl = (
  issuerUuid: Uuid,
  badgeClassUuid: Uuid,
  params?: GetApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertionsParams,
) => getUrl(`/api/v2/issuers/${issuerUuid}/badges/${badgeClassUuid}/assertions`, params);

export const getApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertions = async (
  issuerUuid: Uuid,
  badgeClassUuid: Uuid,
  params?: GetApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertionsParams,
  options?: RequestInit,
): Promise<{ data: Paginated<BadgeAssertionItem>; status: number }> => {
  const res = await fetch(getGetApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertionsUrl(issuerUuid, badgeClassUuid, params), {
    ...options,
    method: 'GET',
  });
  const body = [204, 205, 304].includes(res.status) ? null : await res.text();
  const data = body ? JSON.parse(body) : {};
  return { data, status: res.status };
};
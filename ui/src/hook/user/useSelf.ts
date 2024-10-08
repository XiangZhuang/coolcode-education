import { useQuery } from "@tanstack/react-query";
import { QueryKey } from "../../query/queryKey";
import httpClient from "../../http/httpClient";
import { UserDto } from "../../type/User";
import useAuthenticatedQueryFn from "../auth/useAuthenticatedQueryFn";
import useAccessToken from "../auth/useAccessToken";

const useSelf = () => {
  const { accessToken, clearAccessToken } = useAccessToken();
  const { data, isLoading, error } = useQuery<Readonly<UserDto> | null>({
    queryKey: [QueryKey.USER_SELF],
    queryFn: useAuthenticatedQueryFn(() => httpClient.get("/api/user/self")),
    gcTime: 3600000,
    staleTime: 1800000,
    retry: 0,
    enabled: !!accessToken,
  });

  if (error) {
    clearAccessToken();
  }

  return {
    self: data ?? null,
    isLoading,
    error,
  };
};

export default useSelf;

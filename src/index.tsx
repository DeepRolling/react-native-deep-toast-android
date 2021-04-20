import { NativeModules } from 'react-native';

type DeepToastAndroidType = {
  multiply(a: number, b: number): Promise<number>;
};

const { DeepToastAndroid } = NativeModules;

export default DeepToastAndroid as DeepToastAndroidType;

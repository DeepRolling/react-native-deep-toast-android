import { NativeModules } from 'react-native';

type DeepToastAndroidType = {
  show(params: {
    message: string;
    duration: string;
    position: string;
    addPixelsY?: number;
  }): void;
  hide(): void;
  initialize(): void;
};

const { DeepToastAndroid } = NativeModules;

export default DeepToastAndroid as DeepToastAndroidType;
